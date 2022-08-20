package servlets.Books;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.JSONObject;
import util.DbConnector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String clientUserId = request.getParameter("userId");
        String serverUserId = session.getAttribute("user_id").toString();

        if (clientUserId.equals(serverUserId)) {
            List<Books> booksList = new ArrayList<>();
            String query = "select * from Books";
            try {
                DbConnector.get(query, res -> {
                    while (res.next()) {
                        Books book = new Books();
                        book.setBookId(res.getInt("book_id"));
                        book.setPrice(res.getFloat("price"));
                        book.setAuthor(res.getString("author"));
                        book.setBookName(res.getString("book_name"));
                        book.setCopies(res.getInt("copies"));
                        book.setPublishedYear(res.getInt("published_year"));
                        book.setTotalCopies(res.getInt("total_copies"));
                        booksList.add(book);
                    }
                    return booksList;
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Gson gson = new Gson();
            JsonElement element = gson.toJsonTree(booksList, new TypeToken<List<Books>>() {
            }.getType());
            JsonArray jsonArray = element.getAsJsonArray();
            out.print(jsonArray);
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
            JSONObject bookJson = new JSONObject(json.getString("book"));
            int newCopies = bookJson.getInt("totalCopies");
            String query = "UPDATE Books SET book_name='" + bookJson.getString("bookName") + "',author='" + bookJson.getString("author") + "',copies=IF(" + newCopies + " > total_copies,(" + newCopies + "-total_copies)+copies, " + newCopies + " - (total_copies - copies))" + ",total_copies=" + bookJson.getInt("totalCopies") + ",price=" + bookJson.getFloat("price") + " WHERE book_id='" + bookJson.getInt("bookId") + "'";

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String clientUserId = request.getParameter("userId");
        String serverUserId = session.getAttribute("user_id").toString();

        if (clientUserId.equals(serverUserId)) {
            JSONObject bookJson = new JSONObject(request.getParameter("book"));
            String query = "insert into Books (book_name,author,total_copies,published_year,copies,price) values ('" + bookJson.getString("bookName") + "','" + bookJson.getString("author") + "','" + bookJson.getInt("totalCopies") + "','" + bookJson.getInt("publishedYear") + "','" + bookJson.getInt("totalCopies") + "','" + bookJson.getFloat("price") + "')";
            try {
                int bookID = DbConnector.insert(query);
                out.print("{\"status\":\"1\", \"bookId\":\"" + bookID + "\"}");
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
            int id = json.getInt("bookId");
            String query = "DELETE FROM Books WHERE book_id='" + id + "'";
            try {
                DbConnector.update(query);
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
