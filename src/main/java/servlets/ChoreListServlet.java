package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import models.Chore;
import models.Review;
import services.ChoreService;
import services.ReviewService;
import services.UserService;

@WebServlet("/choreList")
public class ChoreListServlet extends HttpServlet {

	private final ChoreService choreService = new ChoreService();
	private final ReviewService reviewService = new ReviewService();
	private final UserService userService = new UserService();

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
			List<Chore> completedAll = choreService.getCompletedByUser(userId);

			List<Chore> completedByYou = new ArrayList<>();
			List<Chore> postedCompleted = new ArrayList<>();

			if (completedAll != null) {
				for (Chore c : completedAll) {
					String createdBy = c.getCreatedBy() != null ? c.getCreatedBy().trim() : "";
					String acceptedBy = c.getAcceptedBy() != null ? c.getAcceptedBy().trim() : "";

					if (userId.equals(acceptedBy)) {
						completedByYou.add(c);
					}

					if (userId.equals(createdBy)) {
						postedCompleted.add(c);
					}
				}
			}

			Set<String> reviewedChoreIds = new HashSet<>();
			List<Review> reviewsWritten = reviewService.getByReviewer(userId);

			if (reviewsWritten != null) {
				for (Review r : reviewsWritten) {
					if (r.getChoreId() != null && !r.getChoreId().trim().isEmpty()) {
						reviewedChoreIds.add(r.getChoreId());
					}
				}
			}

			Map<String, String> acceptedUserNames = new HashMap<>();

			if (posted != null) {
				for (Chore c : posted) {
					String acceptedBy = c.getAcceptedBy();
					if (acceptedBy != null && !acceptedBy.trim().isEmpty()
							&& !acceptedUserNames.containsKey(acceptedBy)) {
						String fullName = userService.getFullNameById(acceptedBy);
						if (fullName != null && !fullName.trim().isEmpty()) {
							acceptedUserNames.put(acceptedBy, fullName);
						}
					}
				}
			}

			if (postedCompleted != null) {
				for (Chore c : postedCompleted) {
					String acceptedBy = c.getAcceptedBy();
					if (acceptedBy != null && !acceptedBy.trim().isEmpty()
							&& !acceptedUserNames.containsKey(acceptedBy)) {
						String fullName = userService.getFullNameById(acceptedBy);
						if (fullName != null && !fullName.trim().isEmpty()) {
							acceptedUserNames.put(acceptedBy, fullName);
						}
					}
				}
			}

			req.setAttribute("posted", posted);
			req.setAttribute("accepted", accepted);
			req.setAttribute("completedByYou", completedByYou);
			req.setAttribute("postedCompleted", postedCompleted);
			req.setAttribute("reviewedChoreIds", reviewedChoreIds);
			req.setAttribute("acceptedUserNames", acceptedUserNames);

			req.getRequestDispatcher("/WEB-INF/choreList.jsp").forward(req, resp);

		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("error", "Unable to load chores.");
			req.getRequestDispatcher("/WEB-INF/choreList.jsp").forward(req, resp);
		}
	}
}