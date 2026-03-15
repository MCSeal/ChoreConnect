package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import services.ChoreService;

@WebServlet("/publicChoreList")
public class PublicChoreListServlet extends HttpServlet {

	// service for loading public chores
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;
		String userName = session != null ? (String) session.getAttribute("userName") : "Guest";
		String message = req.getParameter("message");

		try {
			// get all public open chores
			List<Chore> publicChores = choreService.getPublicOpenChores();

			// send values to jsp
			req.setAttribute("publicChores", publicChores);
			req.setAttribute("userId", userId);
			req.setAttribute("userName", userName);
			req.setAttribute("message", message);

			req.getRequestDispatcher("/WEB-INF/publicChoreList.jsp").forward(req, resp);

		} catch (SQLException e) {
			e.printStackTrace();

			req.setAttribute("error", "Error loading public chores.");
			req.setAttribute("userId", userId);
			req.setAttribute("userName", userName);
			req.setAttribute("message", message);

			req.getRequestDispatcher("/WEB-INF/publicChoreList.jsp").forward(req, resp);
		}
	}
}