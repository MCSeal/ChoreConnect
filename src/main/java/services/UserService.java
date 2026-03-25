package services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;

import models.User;

public class UserService extends BaseService {

	// Register a new user
	public User register(String email, String fullName, String password)
			throws ServletException, IOException, SQLException {

		String sql = "INSERT INTO users (id, email, full_name, password) VALUES (?, ?, ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			String id = java.util.UUID.randomUUID().toString();

			ps.setString(1, id);
			ps.setString(2, email);
			ps.setString(3, fullName);
			ps.setString(4, password);

			int rows = ps.executeUpdate();

			if (rows == 1) {
				User u = new User();
				u.setId(id);
				u.setEmail(email);
				u.setFullName(fullName);
				u.setPassword(password);
				return u;
			}
		}

		throw new SQLException("User registration failed.");
	}

	// Authenticate user
	public User authenticate(String email, String password) throws ServletException, IOException, SQLException {

		String sql = "SELECT id, email, full_name, password FROM users WHERE email = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String expectedPassword = rs.getString("password");

					if (expectedPassword.equals(password)) {
						User u = new User();
						u.setId(rs.getString("id"));
						u.setEmail(rs.getString("email"));
						u.setFullName(rs.getString("full_name"));
						u.setPassword(password);
						return u;
					}
				}
			}
		}

		// if login fails return null

		return null;
	}

	// Find user by ID
	public User findById(String id) throws ServletException, IOException, SQLException {

		String sql = "SELECT id, email, full_name FROM users WHERE id = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User u = new User();
					u.setId(rs.getString("id"));
					u.setEmail(rs.getString("email"));
					u.setFullName(rs.getString("full_name"));
					return u;
				}
			}
		}

		return null;
	}

	public String getFullNameById(String userId) throws SQLException {
		String sql = "SELECT full_name FROM users WHERE id = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, userId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getString("full_name");
				}
			}
		}

		return null;
	}
}