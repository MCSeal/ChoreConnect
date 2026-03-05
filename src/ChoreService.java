package services;

import models.Chore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChoreService extends BaseService {

	// POSTED BY USER (not completed)
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

	// ACCEPTED BY USER
	public List<Chore> getByAccepted(String userId) throws SQLException {
		String sql = "SELECT * FROM chores WHERE accepted_by = ? AND status = 'ACCEPTED' ORDER BY updated_at DESC";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return mapChores(rs);
			}
		}
	}

	// show complete chores for completed section
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

	// get by id the chores with that id
	public Chore getById(String id) throws SQLException {
		String sql = "SELECT * FROM chores WHERE id = ?";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next())
					return mapChore(rs);
			}
		}
		return null;
	}

	// accept chore
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

	// marking done
	public boolean markDone(String choreId, String userId) throws SQLException {
		String sql = "UPDATE chores SET status='COMPLETED', updated_at=? "
				+ "WHERE id=? AND accepted_by=? AND status='ACCEPTED'";
		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			ps.setString(2, choreId);
			ps.setString(3, userId);
			return ps.executeUpdate() == 1;
		}
	}

	// mapping
	private List<Chore> mapChores(ResultSet rs) throws SQLException {
		List<Chore> list = new ArrayList<>();
		while (rs.next())
			list.add(mapChore(rs));
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

		System.out.println("Attempting DB insert");
		
		String sql = "INSERT INTO chores " + "(title, description, created_by, is_public, latitude, longitude, "
				+ "price_type, hourly_rate, hours, price_amount, status, created_at, updated_at) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN', ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			Timestamp now = new Timestamp(System.currentTimeMillis());

			ps.setString(1, ch.getTitle());
			ps.setString(2, ch.getDescription());
			ps.setString(3, ch.getCreatedBy());
			ps.setBoolean(4, ch.isPublic());
			ps.setDouble(5, ch.getLatitude());
			ps.setDouble(6, ch.getLongitude());

			ps.setString(7, ch.getPriceType());
			ps.setObject(8, ch.getHourlyRate());
			ps.setObject(9, ch.getHours());
			ps.setObject(10, ch.getPriceAmount());

			ps.setTimestamp(11, now);
			ps.setTimestamp(12, now);

			ps.executeUpdate();
		}
	}

	public boolean update(Chore ch, String userId) throws SQLException {
		String sql = "UPDATE chores SET title=?, description=?, is_public=?, latitude=?, longitude=?, updated_at=? "
				+ "WHERE id=? AND created_by=?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, ch.getTitle());
			ps.setString(2, ch.getDescription());
			ps.setBoolean(3, ch.isPublic());
			ps.setDouble(4, ch.getLatitude());
			ps.setDouble(5, ch.getLongitude());
			ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			ps.setString(7, ch.getId());
			ps.setString(8, userId);

			return ps.executeUpdate() == 1;
		}
	}

}