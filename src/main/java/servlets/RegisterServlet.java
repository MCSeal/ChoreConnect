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

        out.println("<!doctype html>");
        out.println("<html lang='en'><head>");
        out.println("<meta charset='utf-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<link rel='icon' type='image/svg+xml' href='" + context + "/favicon.svg'>");
        out.println("<title>Register · ChoreConnect</title>");
        out.println("<link rel='stylesheet' href='" + context + "/styles.css'>");
        out.println("</head><body>");

        out.println("<header class='header container'>");
        out.println("<div class='brand-logo'><img class='brand-logo' src='" + context + "/choreconnect-logo.svg' alt='ChoreConnect Logo'></div>");
        out.println("<div class='brand-title'>ChoreConnect</div>");
        out.println("</header>");

        out.println("<main class='container'>");
        out.println("<section class='card'>");
        out.println("<h1>Create an Account</h1>");

        if (message != null) {
            out.println("<div class='notice error'>" + message + "</div>");
        }

        out.println("<form action='" + context + "/register' method='post' class='form-grid'>");
        out.println("<div><label for='fullName'>Full Name</label>");
        out.println("<input class='input' type='text' id='fullName' name='fullName' maxlength='100' required></div>");

        out.println("<div><label for='email'>Email</label>");
        out.println("<input class='input' type='email' id='email' name='email' maxlength='100' required></div>");

        out.println("<div><label for='password'>Password</label>");
        out.println("<input class='input' type='password' id='password' name='password' minlength='4' required></div>");

        out.println("<div class='actions' style='margin-top:12px'>");
        out.println("<button class='btn btn-primary' type='submit'>Register</button>");
        out.println("</div></form>");

        out.println("<p style='margin-top:1rem;'>Already have an account? <a href='" + context + "/login'>Sign in</a></p>");

        out.println("</section>");
        out.println("<p class='footer'>© 2026 ChoreConnect</p>");
        out.println("</main></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

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