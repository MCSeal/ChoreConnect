package servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req,
                          javax.servlet.http.HttpServletResponse resp) throws IOException {
        if (req.getSession(false) != null) req.getSession(false).invalidate();
        resp.sendRedirect(req.getContextPath() + "/login?message=Logged+out");
    }
}