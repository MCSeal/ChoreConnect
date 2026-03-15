package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.User;
import services.UserService;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String fullName = req.getParameter("fullName");
		String email = req.getParameter("email");
		String password = req.getParameter("password");
		String confirmPassword = req.getParameter("confirmPassword");

		if (fullName == null || email == null || password == null || confirmPassword == null
				|| fullName.trim().isEmpty() || email.trim().isEmpty() || password.isEmpty()
				|| confirmPassword.isEmpty()) {

			req.setAttribute("error", "All fields are required");
			req.setAttribute("fullName", fullName);
			req.setAttribute("email", email);
			req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
			return;
		}

		if (!password.equals(confirmPassword)) {
			req.setAttribute("error", "Passwords do not match");
			req.setAttribute("fullName", fullName);
			req.setAttribute("email", email);
			req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
			return;
		}

		try {
			User u = userService.register(email, fullName, password);

			HttpSession session = req.getSession(true);
			session.setAttribute("userId", u.getId());
			session.setAttribute("userName", u.getFullName());

			resp.sendRedirect(req.getContextPath() + "/choreList");

		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("error", "Registration failed (maybe email already in use)");
			req.setAttribute("fullName", fullName);
			req.setAttribute("email", email);
			req.getRequestDispatcher("/WEB-INF/register.jsp").forward(req, resp);
		}
	}
}