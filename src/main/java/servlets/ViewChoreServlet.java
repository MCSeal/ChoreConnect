package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import services.ChoreService;

@WebServlet("/viewChore")
public class ViewChoreServlet extends HttpServlet {

	// service used to fetch chore data
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		String id = req.getParameter("id");
		String message = req.getParameter("message");

		Chore chore = null;

		try {
			chore = choreService.getById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// send values to jsp
		req.setAttribute("chore", chore);
		req.setAttribute("userId", userId);
		req.setAttribute("message", message);

		req.getRequestDispatcher("/WEB-INF/viewChore.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		String action = req.getParameter("action");
		String id = req.getParameter("id");

		// mark chore as completed
		if ("markDone".equals(action) && userId != null) {

			try {

				boolean success = choreService.markDone(id, userId);

				if (success) {
					resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+completed");
				} else {
					resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + id + "&message=Not+allowed");
				}

				return;

			} catch (SQLException e) {

				e.printStackTrace();
				resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + id + "&message=Database+error");
				return;
			}
		}

		resp.sendRedirect(req.getContextPath() + "/choreList");
	}
}