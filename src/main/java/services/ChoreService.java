
import services.BaseService;
import models.Chore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoreService extends BaseService {

    public List<Chore> getChoresByUser(int ownerId) throws SQLException {
        String sql = "SELECT * FROM chores WHERE created_by = ? ORDER BY created_at DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, ownerId);
            try (ResultSet rs = ps.executeQuery()) {
                return mapChores(rs);
            }
        }
    }

    public List<Chore> listPublicOpen() throws SQLException {
        String sql = "SELECT * FROM chores WHERE is_public = 1 AND status = 'OPEN' ORDER BY created_at DESC";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return mapChores(rs);
        }
    }

    public Chore getById(int id) throws SQLException {
        String sql = "SELECT * FROM chores WHERE id = ?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapChore(rs);
            }
        }
        return null;
    }

    public void create(Chore ch) throws SQLException {
        String sql = "INSERT INTO chores (title, description, created_by, status, is_public) VALUES (?, ?, ?, 'OPEN', ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getTitle());
            ps.setString(2, ch.getDescription());
            ps.setString(3, ch.getCreatedBy());
            ps.setBoolean(4, ch.isPublic());
            ps.executeUpdate();
        }
    }

    public void update(Chore ch, int actingUserId) throws SQLException {
        // Only owner can edit
        String sql = "UPDATE chores SET title=?, description=?, is_public=? WHERE id=? AND created_by=?";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, ch.getTitle());
            ps.setString(2, ch.getDescription());
            ps.setBoolean(3, ch.isPublic());
            ps.setString(4, ch.getId());
            ps.setInt(5, actingUserId);
            ps.executeUpdate();
        }
    }

    public boolean accept(int choreId, int userId) throws SQLException {
        // only accept if currently OPEN and not owned by userId
        String sql = "UPDATE chores SET accepted_by=?, status='ACCEPTED' " +
                     "WHERE id=? AND status='OPEN' AND (created_by <> ?)";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, choreId);
            ps.setInt(3, userId);
            int updated = ps.executeUpdate();
            return updated == 1;
        }
    }

    public boolean markDone(int choreId, int ownerId) throws SQLException {
        String sql = "UPDATE chores SET status='DONE' WHERE id=? AND created_by=? AND status='ACCEPTED'";
        try (Connection c = getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, choreId);
            ps.setInt(2, ownerId);
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
        int accepted = rs.getInt("accepted_by");
     
        ch.setStatus(rs.getString("status"));
        ch.setPublic(rs.getBoolean("is_public"));
        ch.setCreatedAt(rs.getTimestamp("created_at"));
        ch.setUpdatedAt(rs.getTimestamp("updated_at"));
        return ch;
    }
}