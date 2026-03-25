package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import services.ChoreService;
import services.UserService;

@WebServlet("/viewChore")
public class ViewChoreServlet extends HttpServlet {

	// used for loading chore info
	private final ChoreService choreService = new ChoreService();

	// used so ids can turn into real names on the page
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		String id = req.getParameter("id");
		String message = req.getParameter("message");

		Chore chore = null;

		// user id -> full name
		Map<String, String> userNames = new HashMap<>();

		try {
			chore = choreService.getById(id);

			// if we found the chore, grab whatever names we need for display
			if (chore != null) {

				// creator name
				if (chore.getCreatedBy() != null && !chore.getCreatedBy().trim().isEmpty()) {
					String fullName = userService.getFullNameById(chore.getCreatedBy());
					if (fullName != null && !fullName.trim().isEmpty()) {
						userNames.put(chore.getCreatedBy(), fullName);
					}
				}

				// accepted worker name
				if (chore.getAcceptedBy() != null && !chore.getAcceptedBy().trim().isEmpty()) {
					String fullName = userService.getFullNameById(chore.getAcceptedBy());
					if (fullName != null && !fullName.trim().isEmpty()) {
						userNames.put(chore.getAcceptedBy(), fullName);
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// send everything to the jsp
		req.setAttribute("chore", chore);
		req.setAttribute("userId", userId);
		req.setAttribute("message", message);
		req.setAttribute("userNames", userNames);

		req.getRequestDispatcher("/WEB-INF/viewChore.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String choreId = req.getParameter("id");
		String action = req.getParameter("action");

		if (choreId == null || choreId.trim().isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/choreList?message=Missing+chore+ID");
			return;
		}

		try {
			boolean ok = false;
			String message = "Unable to update chore";

			// worker says they finished it
			if ("requestCompletion".equals(action)) {
				ok = choreService.requestCompletion(choreId, userId);
				message = ok ? "Completion+requested" : "Unable+to+request+completion";

				// owner approves it and then it becomes completed
			} else if ("approveCompletion".equals(action)) {
				ok = choreService.approveCompletion(choreId, userId);
				message = ok ? "Chore+marked+completed" : "Unable+to+approve+completion";

				// keeping this in case you still use it somewhere
			} else if ("accept".equals(action)) {
				ok = choreService.accept(choreId, userId);
				message = ok ? "Chore+accepted" : "Unable+to+accept+chore";

			} else {
				resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + choreId + "&message=Unknown+action");
				return;
			}

			resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + choreId + "&message=" + message);

		} catch (SQLException e) {
			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/viewChore?id=" + choreId + "&message=Server+error");
		}
	}
}