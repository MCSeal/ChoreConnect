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

@WebServlet("/chore/edit")
public class EditChoreServlet extends HttpServlet {

	// service for loading and saving chores
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String idStr = req.getParameter("id");
		String message = req.getParameter("message");
		Chore chore = null;

		if (idStr != null && !idStr.isEmpty()) {
			try {
				chore = choreService.getById(idStr);

				// only creator can edit
				if (chore == null || !userId.equals(chore.getCreatedBy())) {
					chore = null;
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		req.setAttribute("chore", chore);
		req.setAttribute("message", message);

		req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String idStr = req.getParameter("id");
		String title = req.getParameter("title");
		String description = req.getParameter("description");
		boolean isPublic = req.getParameter("isPublic") != null;

		double latitude = 0;
		double longitude = 0;

		try {
			String latStr = req.getParameter("latitude");
			String lngStr = req.getParameter("longitude");

			if (latStr != null && !latStr.isEmpty()) {
				latitude = Double.parseDouble(latStr);
			}

			if (lngStr != null && !lngStr.isEmpty()) {
				longitude = Double.parseDouble(lngStr);
			}

		} catch (NumberFormatException e) {
			req.setAttribute("error", "invalid latitude or longitude");
			req.setAttribute("formTitle", title);
			req.setAttribute("formDescription", description);
			req.setAttribute("formIsPublic", isPublic);
			req.setAttribute("formLatitude", req.getParameter("latitude"));
			req.setAttribute("formLongitude", req.getParameter("longitude"));
			req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
			return;
		}

		if (title == null || title.trim().isEmpty()) {
			req.setAttribute("error", "title required");
			req.setAttribute("formTitle", title);
			req.setAttribute("formDescription", description);
			req.setAttribute("formIsPublic", isPublic);
			req.setAttribute("formLatitude", req.getParameter("latitude"));
			req.setAttribute("formLongitude", req.getParameter("longitude"));
			req.getRequestDispatcher("/WEB-INF/editChore.jsp").forward(req, resp);
			return;
		}

		Chore ch = new Chore();
		ch.setId(idStr);
		ch.setTitle(title.trim());
		ch.setDescription(description);
		ch.setPublic(isPublic);
		ch.setLatitude(latitude);
		ch.setLongitude(longitude);
		ch.setCreatedBy(userId);

		try {
			if (idStr == null || idStr.isEmpty()) {
				choreService.create(ch);
				resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+created");
			} else {
				choreService.update(ch, userId);
				resp.sendRedirect(req.getContextPath() + "/choreList?message=Chore+updated");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+saving+chore");
		}
	}
}