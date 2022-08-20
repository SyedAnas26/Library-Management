package servlets;

import util.DbConnector;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");

        boolean success = false;
        HttpSession session = request.getSession(true);

        try {
            String name = request.getParameter("name");
            String password = request.getParameter("password");
            String email = request.getParameter("email");

            String q = "SELECT * FROM User WHERE email='" + email + "'";

            success = (Boolean) DbConnector.get(q, res -> {
                if (!res.next()) {
                    DbConnector.insert("insert into User(name,password,email) values('" + name  + "',SHA('" + password + "'),'" + email + "')");
                    return true;
                }
                return false;
            });

        } catch (SQLException e) {

            out.println("<script type=\"text/javascript\">");
            out.println("alert('User Name Already Exists');");
            out.println("location='signup_page';");
            out.println("</script>");
            e.printStackTrace();
        } catch (Exception e) {
            throw new ServletException("Register Servlet Exception ", e);
        }

        if (success) {

            session.setAttribute("username", request.getParameter("username"));
            out.println("<script type=\"text/javascript\">");
            out.println("alert('Sign up Successful, Log in Now !');");
            out.println("location='login_page';");


        } else {
            out.println("<script type=\"text/javascript\">");
            out.println("alert('User Name Already Exists');");
            out.println("location='signup_page';");

        }
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
        session.invalidate();
    }

}
    


