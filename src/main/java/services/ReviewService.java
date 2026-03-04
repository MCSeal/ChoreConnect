package services;

import services.BaseService;
import models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewService extends BaseService {
    public boolean canOwnerReviewWorker(int choreId, int ownerId) throws SQLException {
        // Owner can review worker only if chore is DONE and owner matches, and worker exists
        String sql = "SELECT 1 FROM chores WHERE id=? AND created_by=? AND status='DONE' AND accepted_by IS NOT NULL";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, choreId);
            ps.setInt(2, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean createOwnerToWorkerReview(int choreId, int ownerId, int rating, String comment) throws SQLException {
        Integer workerId = getWorker(choreId);
        if (workerId == null) return false;

        String sql = "INSERT INTO reviews (chore_id, reviewer_id, reviewee_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, choreId);
            ps.setInt(2, ownerId);
            ps.setInt(3, workerId);
            ps.setInt(4, rating);
            ps.setString(5, comment);
            ps.executeUpdate();
            return true;
        }
    }

    public List<Review> listForUser(int userId) throws SQLException {
        String sql = "SELECT * FROM reviews WHERE reviewee_id=? ORDER BY created_at DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Review> out = new ArrayList<>();
                while (rs.next()) {
                    Review r = new Review();
                    r.setId(rs.getString("id"));
                    r.setChoreId(rs.getInt("chore_id"));
                    r.setReviewerId(rs.getInt("reviewer_id"));
                    r.setRevieweeId(rs.getInt("reviewee_id"));
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    out.add(r);
                }
                return out;
            }
        }
    }

    private Integer getWorker(int choreId) throws SQLException {
        String sql = "SELECT accepted_by FROM chores WHERE id=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, choreId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int v = rs.getInt(1);
                    return rs.wasNull() ? null : v;
                }
            }
        }
        return null;
    }
}