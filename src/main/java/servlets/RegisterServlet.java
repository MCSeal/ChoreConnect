
package servlets;

import models.User;
import services.UserService;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, javax.servlet.http.HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String message = req.getParameter("message");

        out.println("<html><head><title>Register</title></head><body>");
        if (message != null) out.println("<p><b>" + message + "</b></p>");

    }

    @Override
    protected void doPost(javax.servlet.http.HttpServletRequest req,
                          javax.servlet.http.HttpServletResponse resp) throws IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (fullName == null || email == null || password == null
                || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            resp.sendRedirect("register?message=All+fields+are+required");
            return;
        }

        try {
            User u = userService.register(email, fullName, password);
            req.getSession(true).setAttribute("userId", u.getId());
            req.getSession().setAttribute("userName", u.getFullName());
            resp.sendRedirect("choreList?message=Registration+successful");
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("register?message=Registration+failed+(maybe+email+in+use)");
        }
    }
}

