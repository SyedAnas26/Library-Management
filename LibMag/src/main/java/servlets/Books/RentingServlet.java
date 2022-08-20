package servlets.Books;

import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONObject;
import util.DbConnector;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;


public class RentingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws
            ServletException, IOException {

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String clientUserId = request.getParameter("userId");
        String serverUserId = session.getAttribute("user_id").toString();

        if (clientUserId.equals(serverUserId)) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int userId = Integer.parseInt(request.getParameter("userId"));
            int noOfDays = Integer.parseInt(request.getParameter("noOfDays"));
            float bookPrice = 0;
            LocalDate start = LocalDate.now();
            LocalDate end = start.plusDays(noOfDays);
            String query = "select * from Books where book_id ='" + bookId + "'";

            try {
                bookPrice = (float) DbConnector.get(query, res -> {
                    if (res.next()) {
                        return res.getFloat("price");
                    }
                    return 0;
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            String query2 = "insert into RentedBooks (book_id,issue_date,due_date,cost,user_id) values ('" + bookId + "','" + start + "','" + end + "','" + (noOfDays * bookPrice) + "','" + userId + "')";
            try {
                DbConnector.insert(query2);
                String updateBookQuery = "update Books set copies = copies - 1 where book_id='" + bookId + "'";
                DbConnector.update(updateBookQuery);
                out.print("{\"status\":\"1\"}");
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"status\":\"0\"}");
            }
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject(inputStreamToString(request.getInputStream()));
        String clientUserId = json.getString("userId");
        String serverUserId = session.getAttribute("user_id").toString();

        if (clientUserId.equals(serverUserId)) {
            int status = json.getInt("status");
            int rentedBookId = json.getInt("rentedBookId");
            // Status Can have the following values
            //    0 - Pending for Issue
            //    1 - Issued
            //    2 - Returned
            String query = "update RentedBooks set status='" + status + "' where rented_book_id='" + rentedBookId + "'";
            try {
                DbConnector.update(query);
                out.print("{\"status\":\"1\"}");
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"status\":\"0\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JSONObject json = new JSONObject(inputStreamToString(request.getInputStream()));
        String clientUserId = json.getString("userId");
        String serverUserId = session.getAttribute("user_id").toString();

        if (clientUserId.equals(serverUserId)) {
            int rentingBookId = json.getInt("rentedBookId");
            int bookId = json.getInt("bookId");
            String query = "DELETE FROM RentedBooks WHERE rented_book_id='" + rentingBookId + "'";
            String updateBookQuery = "update Books set copies=copies + 1 where book_id='" + bookId + "'";
            try {
                DbConnector.update(query);
                DbConnector.update(updateBookQuery);
                out.print("{\"status\":\"1\"}");
            } catch (Exception e) {
                e.printStackTrace();
                out.print("{\"status\":\"0\"}");
            }
        }
    }

    private static String inputStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream, "UTF-8");
        return scanner.hasNext() ? scanner.useDelimiter("\\A").next() : "";
    }
}
