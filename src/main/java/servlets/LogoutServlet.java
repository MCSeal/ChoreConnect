package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/signout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Invalidate the current session
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        // Redirect to login page
        resp.sendRedirect(req.getContextPath() + "/login.html");
    }
}