package servlets.Users;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import javax.servlet.*;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONObject;
import util.DbConnector;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (request.getParameter("userId").equals(String.valueOf(session.getAttribute("user_id")))) {
            if (request.getServletPath().equals("/getUserList")) {
                List<User> userList = new ArrayList<>();
                String query = "select User.user_id,User.is_admin,User.email,User.name, COUNT(RentedBooks.book_id) AS NumberOfBooks from RentedBooks Left Join User on RentedBooks.user_id = User.user_id group by RentedBooks.user_id;";
                try {
                    DbConnector.get(query, res -> {
                        while (res.next()) {
                            User user = new User();
                            user.setName(res.getString("name"));
                            user.setEmail(res.getString("email"));
                            user.setId(res.getInt("user_id"));
                            user.setAdmin(res.getBoolean("is_admin"));
                            user.setNoOfBooks(res.getInt("NumberOfBooks"));
                            userList.add(user);
                        }
                        return userList;
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Gson gson = new Gson();
                JsonElement element = gson.toJsonTree(userList, new TypeToken<List<User>>() {
                }.getType());
                JsonArray jsonArray = element.getAsJsonArray();
                out.print(jsonArray);
            } else if (request.getServletPath().equals("/getUserBooks")) {
                String email = request.getParameter("email");
                String query = "Select user_id,name from User where email='" + email + "'";

                int userId = 0;
                String name = "";
                try {
                    userId = (int) DbConnector.get(query, res -> {
                        if (res.next()) {
                            return res.getInt("user_id");
                        }
                        return 0;
                    });

                    name = (String) DbConnector.get(query, res -> {
                        if (res.next()) {
                            return res.getString("name");
                        }
                        return null;
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                JSONObject returnJSON = new JSONObject();
                returnJSON.put("userName", name);
                HashMap<Integer, String> statusMaps = new HashMap<>();
                statusMaps.put(0, "Pending Issue");
                statusMaps.put(1, "Issued");
                statusMaps.put(2, "Returned");

                String query2 = "select Books.book_id,rented_book_id,published_year,book_name,author,copies,price,due_date,issue_date,status,cost,DATEDIFF(due_date, issue_date) AS days_left from (Books INNER JOIN RentedBooks on Books.book_id = RentedBooks.book_id) where user_id =" + userId;
                JSONArray jsonArray = new JSONArray();
                try {
                    DbConnector.get(query2, res -> {
                        while (res.next()) {
                            JSONObject json = new JSONObject();
                            json.put("bookId", res.getString("book_id"));
                            json.put("bookName", res.getString("book_name"));
                            json.put("author", res.getString("author"));
                            json.put("publishedYear", res.getInt("published_year"));
                            json.put("copies", res.getInt("copies"));
                            json.put("price", res.getFloat("cost"));
                            json.put("dueDate", res.getDate("due_date"));
                            json.put("issueDate", res.getDate("issue_date"));
                            json.put("rentedBookId", res.getInt("rented_book_id"));
                            json.put("daysLeft", res.getInt("days_left"));
                            json.put("status", statusMaps.get(res.getInt("status")));
                            jsonArray.put(json);
                        }
                        return jsonArray;
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                returnJSON.put("userBookList", jsonArray);
                out.print(returnJSON);
            } else if (request.getServletPath().equals("/dashboardDetails")) {
                JSONObject json = new JSONObject();
                String query = "SELECT SUM(total_copies) AS total_books,SUM(copies) AS available_books from Books";
                String query2 = "SELECT COUNT(user_id) AS total_users from User";
                try {
                    DbConnector.get(query, res -> {
                        if (res.next()) {
                            json.put("totalBooks", res.getInt("total_books"));
                            json.put("availableBooks", res.getInt("available_books"));
                        }
                        return json;
                    });

                    DbConnector.get(query2, res -> {
                        if (res.next()) {
                            json.put("totalUsers", res.getInt("total_users"));
                        }
                        return json;
                    });
                    out.print(json);

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}