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
		req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String email = req.getParameter("email");
		String password = req.getParameter("password");

		// trim email so something like "test@email.com " doesn't break login
		if (email != null) {
			email = email.trim();
		}

		// quick check so we don't hit the DB for empty inputs
		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			req.setAttribute("error", "Email and password are required");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
			return;
		}

		try {
			User u = userService.authenticate(email, password);

			if (u != null) {

				// invalidate old session just in case (cleaner login flow)
				HttpSession oldSession = req.getSession(false);
				if (oldSession != null) {
					oldSession.invalidate();
				}

				HttpSession session = req.getSession(true);
				session.setAttribute("userId", u.getId());
				session.setAttribute("userName", u.getFullName());

				resp.sendRedirect(req.getContextPath() + "/choreList");

			} else {
				// don't say what exactly failed (email vs password)
				req.setAttribute("error", "Invalid email or password");
				req.setAttribute("email", email);
				req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
			}

		} catch (Exception e) {
			e.printStackTrace();

			// generic message so we don't expose internal issues
			req.setAttribute("error", "Something went wrong. Please try again.");
			req.setAttribute("email", email);
			req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
		}
	}
}