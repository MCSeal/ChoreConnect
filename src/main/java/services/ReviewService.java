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
		String sql = "SELECT 1 FROM chores WHERE id=? AND created_by=? AND status='DONE' AND accepted_by IS NOT NULL";

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
		if (workerId == null) {
			return false;
		}

		String sql = "INSERT INTO reviews (chore_id, reviewer_id, reviewee_id, rating, comment) VALUES (?, ?, ?, ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, choreId);
			ps.setString(2, userId);
			ps.setString(3, workerId);
			ps.setInt(4, rating);
			ps.setString(5, comment);
			ps.executeUpdate();
			return true;
		}
	}

	public List<Review> listForUser(String userId) throws SQLException {
		String sql = "SELECT * FROM reviews WHERE reviewee_id=? ORDER BY created_at DESC";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				List<Review> out = new ArrayList<>();

				while (rs.next()) {
					Review r = new Review();
					r.setId(rs.getString("id"));
					r.setChoreId(rs.getString("chore_id"));
					r.setReviewerId(rs.getString("reviewer_id"));
					r.setRevieweeId(rs.getString("reviewee_id"));
					r.setRating(rs.getInt("rating"));
					r.setComment(rs.getString("comment"));
					r.setCreatedAt(rs.getTimestamp("created_at"));
					out.add(r);
				}

				return out;
			}
		}
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