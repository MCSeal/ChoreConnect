package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Review;

public class ReviewService extends BaseService {

	public boolean canOwnerReviewWorker(String choreId, String userId) throws SQLException {
		// owner can review worker only if chore is done, belongs to them, and has an
		// accepted worker
		String sql = "SELECT 1 FROM chores WHERE id=? AND created_by=? AND status='COMPLETED' AND accepted_by IS NOT NULL";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, choreId);
			ps.setString(2, userId);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	public boolean createOwnerToWorkerReview(String choreId, String userId, int rating, String comment)
			throws SQLException {

		String workerId = getWorker(choreId);
		if (workerId == null || workerId.trim().isEmpty()) {
			return false;
		}

		String reviewId = java.util.UUID.randomUUID().toString();

		String sql = "INSERT INTO reviews (id, chore_id, reviewer_id, reviewee_id, rating, comment) VALUES (?, ?, ?, ?, ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, reviewId);
			ps.setString(2, choreId);
			ps.setString(3, userId);
			ps.setString(4, workerId);
			ps.setInt(5, rating);
			ps.setString(6, comment);
			ps.executeUpdate();
			return true;
		}
	}

	// reviews received by this user
	public List<Review> listForUser(String userId) throws SQLException {
		String sql = "SELECT * FROM reviews WHERE reviewee_id=? ORDER BY created_at DESC";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				List<Review> out = new ArrayList<>();

				while (rs.next()) {
					out.add(mapReview(rs));
				}

				return out;
			}
		}
	}

	// reviews written by this user
	public List<Review> getByReviewer(String userId) throws SQLException {
		String sql = "SELECT * FROM reviews WHERE reviewer_id=? ORDER BY created_at DESC";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				List<Review> out = new ArrayList<>();

				while (rs.next()) {
					out.add(mapReview(rs));
				}

				return out;
			}
		}
	}

	// optional helper if later you want to check one chore quickly
	public Review getByChoreIdAndReviewer(String choreId, String reviewerId) throws SQLException {
		String sql = "SELECT * FROM reviews WHERE chore_id=? AND reviewer_id=? LIMIT 1";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, choreId);
			ps.setString(2, reviewerId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Review r = new Review();
					r.setId(rs.getString("id"));
					r.setChoreId(rs.getString("chore_id"));
					r.setReviewerId(rs.getString("reviewer_id"));
					r.setRevieweeId(rs.getString("reviewee_id"));
					r.setRating(rs.getInt("rating"));
					r.setComment(rs.getString("comment"));
					r.setCreatedAt(rs.getTimestamp("created_at"));
					return r;
				}
			}
		}

		return null;
	}

	private Review mapReview(ResultSet rs) throws SQLException {
		Review r = new Review();
		r.setId(rs.getString("id"));
		r.setChoreId(rs.getString("chore_id"));
		r.setReviewerId(rs.getString("reviewer_id"));
		r.setRevieweeId(rs.getString("reviewee_id"));
		r.setRating(rs.getInt("rating"));
		r.setComment(rs.getString("comment"));
		r.setCreatedAt(rs.getTimestamp("created_at"));
		return r;
	}

	private String getWorker(String choreId) throws SQLException {
		String sql = "SELECT accepted_by FROM chores WHERE id=?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, choreId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString(1);
				}
			}
		}

		return null;
	}
}