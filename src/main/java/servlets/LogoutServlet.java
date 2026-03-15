package servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		resp.sendRedirect(req.getContextPath() + "/login");
	}
}