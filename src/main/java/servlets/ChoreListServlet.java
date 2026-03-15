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

@WebServlet("/choreList")
public class ChoreListServlet extends HttpServlet {

	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		try {
			List<Chore> posted = choreService.getByCreator(userId);
			List<Chore> accepted = choreService.getByAccepted(userId);
			List<Chore> completed = choreService.getCompletedByUser(userId);

			req.setAttribute("posted", posted);
			req.setAttribute("accepted", accepted);
			req.setAttribute("completed", completed);

			req.getRequestDispatcher("/WEB-INF/choreList.jsp").forward(req, resp);

		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("error", "Unable to load chores.");
			req.getRequestDispatcher("/WEB-INF/choreList.jsp").forward(req, resp);
		}
	}
}