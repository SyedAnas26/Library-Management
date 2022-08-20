package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;

public class LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(true);
        session.setAttribute("user_id", null);
        session.setAttribute("email", null);
        session.setAttribute("is_admin", null);
        session.invalidate();

        out.println("<script type=\"text/javascript\">");
        out.println("alert('Logout Successful');");
        out.println("location='login_page';");
        out.println("</script>");

    }

}
