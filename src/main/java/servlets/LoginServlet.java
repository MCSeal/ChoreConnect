package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;
import services.UserService;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.getRequestDispatcher("/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String email = req.getParameter("email");
		String password = req.getParameter("password");

		try {
			User u = userService.authenticate(email, password);

			if (u != null) {
				HttpSession session = req.getSession(true);
				session.setAttribute("userId", u.getId().toString());
				session.setAttribute("userName", u.getFullName());

				resp.sendRedirect(req.getContextPath() + "/choreList");
			} else {
				req.setAttribute("error", "Invalid credentials");
				req.setAttribute("email", email);
				req.getRequestDispatcher("/login.jsp").forward(req, resp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Server error");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/login.jsp").forward(req, resp);
		}
	}
}