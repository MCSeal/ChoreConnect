package servlets;

import models.User;
import services.UserService;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String message = req.getParameter("message");

        out.println("<html><head><title>Login</title></head><body>");
        
        if (message != null) {
            out.println("<p style='color:red;'><b>" + message + "</b></p>");
        }


    }

    	

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

            String email = req.getParameter("email");
            String password = req.getParameter("password");

            try {
                User u = userService.authenticate(email, password);

                if (u != null) {
                    HttpSession session = req.getSession(true);
                    session.setAttribute("userId", u.getId());
                    session.setAttribute("userName", u.getFullName());

                    resp.sendRedirect(req.getContextPath() + 
                        "/choreList?message=Welcome+" +
                        java.net.URLEncoder.encode(u.getFullName(), "UTF-8"));

                } else {
                    resp.sendRedirect(req.getContextPath() +
                        "/login?message=Invalid+credentials");
                }

            } catch (Exception e) {  
                e.printStackTrace(); 
                resp.sendRedirect(req.getContextPath() +
                    "/login?message=Server+error");
            }
        }
    }