package servlets;

import util.DbConnector;

import java.io.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;


public class LoginServlet extends HttpServlet {

    public void init() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getServletPath().equals("/getUserId")) {

            HttpSession sess = request.getSession();
            String user = "{\"user_id\":\"" + sess.getAttribute("user_id") + "\",\"email\":\"" + sess.getAttribute("email") + "\",\"isAdmin\":\"" + sess.getAttribute("is_admin") + "\"}";
            PrintWriter out = response.getWriter();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(user);

        } else {
            HttpSession sess = request.getSession(true);
            response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            String email = null;
            int user_id = 0;
            boolean success = false;
            boolean isAdmin = false;

            String thisName = request.getParameter("email");
            String thisPwd = request.getParameter("password");
            String query = "SELECT * FROM User WHERE email='" + thisName + "' and password= SHA('" + thisPwd + "')";

            try {

                email = (String) DbConnector.get(query, res -> {
                    try {
                        if (res.next()) {
                            return res.getString("email");
                        }
                    } catch (Exception e) {
                        throw new ServletException("Login Servlet Exception.", e);
                    }
                    return null;
                });

                user_id = (int) DbConnector.get(query, res -> {
                    try {
                        if (res.next()) {
                            return res.getInt("user_id");
                        }
                    } catch (Exception e) {
                        throw new ServletException("Exception in Login Servlet ", e);
                    }
                    return null;
                });

                isAdmin = (Boolean) DbConnector.get(query, res -> {
                    try {
                        if (res.next()) {
                            return res.getBoolean("is_admin");
                        }
                    } catch (Exception e) {
                        throw new ServletException("Exception in Login Servlet ", e);
                    }
                    return null;
                });
            } catch (Exception e) {
                System.out.println("Exception in Login Servlet " + e);
            }

            if (email != null) {
                success = true;
            }

            if (success) {
                sess.setAttribute("user_id", user_id);
                sess.setAttribute("email", email);
                sess.setAttribute("is_admin", isAdmin);

                response.sendRedirect("dashboard");

            } else {

                out.println("<script type=\"text/javascript\">");
                out.println("alert('Incorrect UserId or Password');");
                out.println("location='login_page';");
                out.println("</script>");
                sess.invalidate();

            }
        }


    }
}