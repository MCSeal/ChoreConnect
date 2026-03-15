package servlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import services.ReviewService;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

	// service that handles reviews
	private final ReviewService reviewService = new ReviewService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		String choreIdStr = req.getParameter("choreId");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		if (choreIdStr == null) {
			req.setAttribute("error", "Missing choreId");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			return;
		}

		try {

			String choreId = choreIdStr;

			// check if the owner can review the worker
			if (!reviewService.canOwnerReviewWorker(choreId, userId)) {
				req.setAttribute("error", "Cannot review: chore not DONE or not your chore.");
			}

			req.setAttribute("choreId", choreId);

		} catch (NumberFormatException | SQLException e) {

			req.setAttribute("error", "Invalid choreId");
		}

		req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		String userId = (String) req.getSession().getAttribute("userId");
		String choreIdStr = req.getParameter("choreId");
		String ratingStr = req.getParameter("rating");
		String comment = req.getParameter("comment");

		try {

			String choreId = choreIdStr;
			int rating = Integer.parseInt(ratingStr);

			if (rating < 1 || rating > 5) {
				throw new IllegalArgumentException();
			}

			if (reviewService.canOwnerReviewWorker(choreId, userId)) {

				boolean ok = reviewService.createOwnerToWorkerReview(choreId, userId, rating, comment);

				if (ok) {
					resp.sendRedirect(req.getContextPath() + "/choreList?message=Review+submitted");
					return;
				}
			}

			resp.sendRedirect(req.getContextPath() + "/choreList?message=Unable+to+submit+review");

		} catch (SQLException | IllegalArgumentException e) {

			e.printStackTrace();
			resp.sendRedirect(req.getContextPath() + "/choreList?message=Error+submitting+review");
		}
	}
}