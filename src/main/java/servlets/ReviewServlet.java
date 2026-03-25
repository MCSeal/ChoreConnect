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

	private final ReviewService reviewService = new ReviewService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;
		String choreId = req.getParameter("choreId");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		if (choreId == null || choreId.trim().isEmpty()) {
			req.setAttribute("error", "Missing chore ID.");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			return;
		}

		try {
			if (!reviewService.canOwnerReviewWorker(choreId, userId)) {
				req.setAttribute("error", "You cannot review this chore.");
			} else if (reviewService.getByChoreIdAndReviewer(choreId, userId) != null) {
				req.setAttribute("error", "You already submitted a review for this chore.");
			}

			req.setAttribute("choreId", choreId);
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);

		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("error", "Unable to load review form.");
			req.setAttribute("choreId", choreId);
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		String userId = session != null ? (String) session.getAttribute("userId") : null;

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		String choreId = req.getParameter("choreId");
		String ratingStr = req.getParameter("rating");
		String comment = req.getParameter("comment");

		req.setAttribute("choreId", choreId);
		req.setAttribute("rating", ratingStr);
		req.setAttribute("comment", comment);

		if (choreId == null || choreId.trim().isEmpty()) {
			req.setAttribute("error", "Missing chore ID.");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			return;
		}

		int rating;
		try {
			rating = Integer.parseInt(ratingStr);
		} catch (Exception e) {
			req.setAttribute("error", "Rating must be a number from 1 to 5.");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			return;
		}

		if (rating < 1 || rating > 5) {
			req.setAttribute("error", "Rating must be between 1 and 5.");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			return;
		}

		try {
			if (!reviewService.canOwnerReviewWorker(choreId, userId)) {
				req.setAttribute("error", "You cannot review this chore.");
				req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
				return;
			}

			if (reviewService.getByChoreIdAndReviewer(choreId, userId) != null) {
				req.setAttribute("error", "You already submitted a review for this chore.");
				req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
				return;
			}

			boolean ok = reviewService.createOwnerToWorkerReview(choreId, userId, rating, comment);

			if (ok) {
				resp.sendRedirect(req.getContextPath() + "/choreList?message=Review+submitted");
			} else {
				req.setAttribute("error", "Unable to submit review.");
				req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			req.setAttribute("error", "Error submitting review.");
			req.getRequestDispatcher("/WEB-INF/review.jsp").forward(req, resp);
		}
	}
}