package services;

import models.Chore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoreService extends BaseService {

    // Get chores created by a specific user
    public List<Chore> getChoresByUser(String userId) throws SQLException {
        String sql = "SELECT * FROM chores WHERE created_by = ? ORDER BY created_at DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapChores(rs);
            }
        }
    }

    // Get public and open chores
    public List<Chore> getPublicOpenChores() throws SQLException {
        String sql = "SELECT * FROM chores WHERE is_public = 1 AND status = 'OPEN' ORDER BY created_at DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return mapChores(rs);
        }
    }

    // Get chore by ID
    public Chore getById(String id) throws SQLException {
        String sql = "SELECT * FROM chores WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapChore(rs);
            }
        }
        return null;
    }

    // Create a chore
    public void create(Chore ch) throws SQLException {
        String sql = "INSERT INTO chores (id, title, description, created_by, status, is_public, latitude, longitude, created_at, updated_at) " +
                     "VALUES (?, ?, ?, ?, 'OPEN', ?, ?, ?, ?, ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getId());
            ps.setString(2, ch.getTitle());
            ps.setString(3, ch.getDescription());
            ps.setString(4, ch.getCreatedBy());
            ps.setBoolean(5, ch.isPublic());
            ps.setDouble(6, ch.getLatitude());
            ps.setDouble(7, ch.getLongitude());
            ps.setTimestamp(8, ch.getCreatedAt());
            ps.setTimestamp(9, ch.getUpdatedAt());
            ps.executeUpdate();
        }
    }

    // Update a chore (only owner can edit)
    public void update(Chore ch, String actingUserId) throws SQLException {
        String sql = "UPDATE chores SET title=?, description=?, is_public=?, latitude=?, longitude=?, updated_at=? " +
                     "WHERE id=? AND created_by=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getTitle());
            ps.setString(2, ch.getDescription());
            ps.setBoolean(3, ch.isPublic());
            ps.setDouble(4, ch.getLatitude());
            ps.setDouble(5, ch.getLongitude());
            ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            ps.setString(7, ch.getId());
            ps.setString(8, actingUserId);
            ps.executeUpdate();
        }
    }

    // Accept chore
    public boolean accept(String choreId, String userId) throws SQLException {
        String sql = "UPDATE chores SET accepted_by=?, status='ACCEPTED' " +
                     "WHERE id=? AND status='OPEN' AND created_by<>?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, choreId);
            ps.setString(3, userId);
            return ps.executeUpdate() == 1;
        }
    }

    // Mark chore done
    public boolean markDone(String choreId, String ownerId) throws SQLException {
        String sql = "UPDATE chores SET status='DONE' WHERE id=? AND created_by=? AND status='ACCEPTED'";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, choreId);
            ps.setString(2, ownerId);
            return ps.executeUpdate() == 1;
        }
    }

    private List<Chore> mapChores(ResultSet rs) throws SQLException {
        List<Chore> list = new ArrayList<>();
        while (rs.next()) list.add(mapChore(rs));
        return list;
    }

    private Chore mapChore(ResultSet rs) throws SQLException {
        Chore ch = new Chore();
        ch.setId(rs.getString("id"));
        ch.setTitle(rs.getString("title"));
        ch.setDescription(rs.getString("description"));
        ch.setCreatedBy(rs.getString("created_by"));
        ch.setAcceptedBy(rs.getString("accepted_by"));
        ch.setStatus(rs.getString("status"));
        ch.setPublic(rs.getBoolean("is_public"));
        ch.setLatitude(rs.getDouble("latitude"));
        ch.setLongitude(rs.getDouble("longitude"));
        ch.setCreatedAt(rs.getTimestamp("created_at"));
        ch.setUpdatedAt(rs.getTimestamp("updated_at"));
        return ch;
    }
}