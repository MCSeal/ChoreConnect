package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import models.Chore;

public class ChoreService extends BaseService {

	// chores created by this user that are not fully completed yet
	public List<Chore> getByCreator(String userId) throws SQLException {
		String sql = "SELECT * FROM chores WHERE created_by = ? AND status <> 'COMPLETED' ORDER BY created_at DESC";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return mapChores(rs);
			}
		}
	}

	public List<Chore> getPublicOpenChores() throws SQLException {
		String sql = "SELECT * FROM chores WHERE is_public = 1 AND status = 'OPEN' ORDER BY created_at DESC";

		try (Connection c = getConnection();
				PreparedStatement ps = c.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			return mapChores(rs);
		}
	}

	// chores accepted by this user and still active
	public List<Chore> getByAccepted(String userId) throws SQLException {
		String sql = "SELECT * FROM chores WHERE accepted_by = ? AND status = 'ACCEPTED' ORDER BY updated_at DESC";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return mapChores(rs);
			}
		}
	}

	// completed chores tied to this user, either as creator or worker
	public List<Chore> getCompletedByUser(String userId) throws SQLException {
		String sql = "SELECT * FROM chores WHERE status = 'COMPLETED' AND (created_by = ? OR accepted_by = ?) ORDER BY updated_at DESC";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			ps.setString(2, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return mapChores(rs);
			}
		}
	}

	// get one chore by id
	public Chore getById(String id) throws SQLException {
		String sql = "SELECT * FROM chores WHERE id = ?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapChore(rs);
				}
			}
		}
		return null;
	}

	// accept an open chore
	public boolean accept(String choreId, String userId) throws SQLException {
		String sql = "UPDATE chores SET accepted_by=?, status='ACCEPTED', updated_at=? "
				+ "WHERE id=? AND status='OPEN' AND created_by<>?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			ps.setString(3, choreId);
			ps.setString(4, userId);
			return ps.executeUpdate() == 1;
		}
	}

	// worker says they are done, waiting for owner approval
	public boolean requestCompletion(String choreId, String userId) throws SQLException {
		String sql = "UPDATE chores SET status='PENDING_COMPLETION', updated_at=? "
				+ "WHERE id=? AND accepted_by=? AND status='ACCEPTED'";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			ps.setString(2, choreId);
			ps.setString(3, userId);
			return ps.executeUpdate() == 1;
		}
	}

	// owner approves and then it becomes completed
	public boolean approveCompletion(String choreId, String userId) throws SQLException {
		String sql = "UPDATE chores SET status='COMPLETED', updated_at=? "
				+ "WHERE id=? AND created_by=? AND status='PENDING_COMPLETION'";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			ps.setString(2, choreId);
			ps.setString(3, userId);
			return ps.executeUpdate() == 1;
		}
	}

	// helper for mapping a whole result set
	private List<Chore> mapChores(ResultSet rs) throws SQLException {
		List<Chore> list = new ArrayList<>();
		while (rs.next()) {
			list.add(mapChore(rs));
		}
		return list;
	}

	// helper for turning one row into a chore object
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

		// price stuff
		ch.setPriceType(rs.getString("price_type"));

		Double hourlyRate = rs.getObject("hourly_rate") != null ? rs.getDouble("hourly_rate") : null;
		ch.setHourlyRate(hourlyRate);

		Integer hours = rs.getObject("hours") != null ? rs.getInt("hours") : null;
		ch.setHours(hours);

		Double priceAmount = rs.getObject("price_amount") != null ? rs.getDouble("price_amount") : null;
		ch.setPriceAmount(priceAmount);

		return ch;
	}

	public void create(Chore ch) throws SQLException {
		System.out.println("Creating chore: " + ch.getTitle());
		System.out.println("Attempting DB insert");

		String sql = "INSERT INTO chores " + "(id, title, description, created_by, is_public, latitude, longitude, "
				+ "price_type, hourly_rate, hours, price_amount, status, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN', ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, ch.getId());
			ps.setString(2, ch.getTitle());
			ps.setString(3, ch.getDescription());
			ps.setString(4, ch.getCreatedBy());
			ps.setBoolean(5, ch.isPublic());
			ps.setDouble(6, ch.getLatitude());
			ps.setDouble(7, ch.getLongitude());
			ps.setString(8, ch.getPriceType());
			ps.setObject(9, ch.getHourlyRate());
			ps.setObject(10, ch.getHours());
			ps.setObject(11, ch.getPriceAmount());

			Timestamp now = new Timestamp(System.currentTimeMillis());
			ps.setTimestamp(12, now);
			ps.setTimestamp(13, now);

			ps.executeUpdate();
		}
	}

	public boolean update(Chore ch, String userId) throws SQLException {

		// first grab the existing chore so we can keep old price info
		// if it already got accepted or is past that point
		Chore existing = getById(ch.getId());
		if (existing == null || !userId.equals(existing.getCreatedBy())) {
			return false;
		}

		boolean canEditPrice = "OPEN".equals(existing.getStatus());

		String sql = "UPDATE chores SET " + "title=?, description=?, is_public=?, latitude=?, longitude=?, "
				+ "price_type=?, hourly_rate=?, hours=?, price_amount=?, updated_at=? " + "WHERE id=? AND created_by=?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, ch.getTitle());
			ps.setString(2, ch.getDescription());
			ps.setBoolean(3, ch.isPublic());
			ps.setDouble(4, ch.getLatitude());
			ps.setDouble(5, ch.getLongitude());

			// only let price values change while still open
			if (canEditPrice) {
				ps.setString(6, ch.getPriceType());
				ps.setObject(7, ch.getHourlyRate());
				ps.setObject(8, ch.getHours());
				ps.setObject(9, ch.getPriceAmount());
			} else {
				ps.setString(6, existing.getPriceType());
				ps.setObject(7, existing.getHourlyRate());
				ps.setObject(8, existing.getHours());
				ps.setObject(9, existing.getPriceAmount());
			}

			ps.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
			ps.setString(11, ch.getId());
			ps.setString(12, userId);

			return ps.executeUpdate() == 1;
		}
	}
}