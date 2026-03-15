package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import services.ChoreService;

@WebServlet("/chore/accept")
public class AcceptChoreServlet extends HttpServlet {

	// service used to accept the chore
	private final ChoreService choreService = new ChoreService();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;
		String choreId = req.getParameter("choreId");

		String message;
		String messageType;

		// make sure login and chore id exist
		if (userId == null || choreId == null || choreId.trim().isEmpty()) {
			message = "Missing user or chore info.";
			messageType = "error";
		} else {
			try {
				boolean success = choreService.accept(choreId, userId);

				if (success) {
					message = "You have successfully accepted this chore!";
					messageType = "success";
				} else {
					message = "Unable to accept this chore. It might already be taken.";
					messageType = "error";
				}

			} catch (SQLException e) {
				e.printStackTrace();
				message = "Error accepting chore.";
				messageType = "error";
			}
		}

		req.setAttribute("message", message);
		req.setAttribute("messageType", messageType);

		req.getRequestDispatcher("/WEB-INF/acceptChore.jsp").forward(req, resp);
	}
}