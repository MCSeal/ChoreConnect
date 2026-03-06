package servlets;

import models.User;
import services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String context = req.getContextPath();
        String message = req.getParameter("message");

        out.println("<html><head><title>Login</title></head><body>");
        
        if (message != null) {
            out.println("<p style='color:red;'><b>" + message + "</b></p>");
        }
        out.println("</main></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (!password.equals(confirmPassword)) {
            resp.sendRedirect(req.getContextPath() + "/register?message=Passwords+do+not+match");
            return;
        }
        if (fullName == null || email == null || password == null
                || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/register?message=All+fields+are+required");
            return;
        }

        try {
            User u = userService.register(email, fullName, password);
            req.getSession(true).setAttribute("userId", u.getId());
            req.getSession().setAttribute("userName", u.getFullName());
            resp.sendRedirect(req.getContextPath() + "/choreList?message=Registration+successful");
        } catch (ServletException | IOException | SQLException e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/register?message=Registration+failed+(maybe+email+in+use)");
        }
    }
}