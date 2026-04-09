package services;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;

import models.User;

public class UserService extends BaseService {

	// Register a new user
	public User register(String email, String fullName, String password)
			throws ServletException, IOException, SQLException {

		// stop early if the email is already taken
		if (emailExists(email)) {
			throw new SQLException("Email already in use.");
		}

		String sql = "INSERT INTO users (id, email, full_name, password) VALUES (?, ?, ?, ?)";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			String uuid = UUID.randomUUID().toString();

			// save the hashed version instead of the plain password
			String hashedPassword = PasswordUtil.hashPassword(password);

			ps.setString(1, uuid);
			ps.setString(2, email);
			ps.setString(3, fullName);
			ps.setString(4, hashedPassword);

			int rows = ps.executeUpdate();

			if (rows == 1) {
				User u = new User();
				u.setId(uuid);
				u.setEmail(email);
				u.setFullName(fullName);

				// no need to keep a password on the returned user object
				u.setPassword(null);
				return u;
			}
		}

		throw new SQLException("User registration failed.");
	}

	// check if an email already exists before trying the insert
	public boolean emailExists(String email) throws SQLException {
		String sql = "SELECT 1 FROM users WHERE email = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	// Authenticate user
	public User authenticate(String email, String password) throws ServletException, IOException, SQLException {

		String sql = "SELECT id, email, full_name, password FROM users WHERE email = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					String savedHash = rs.getString("password");

					// compare typed password to the stored hash
					if (PasswordUtil.checkPassword(password, savedHash)) {
						User u = new User();

						u.setId(rs.getString("id"));
						u.setEmail(rs.getString("email"));
						u.setFullName(rs.getString("full_name"));
						u.setPassword(null);
						return u;
					}
				}
			}
		}

		return null;
	}

	// Find user by ID
	public User findById(UUID id) throws ServletException, IOException, SQLException {

		String sql = "SELECT id, email, full_name FROM users WHERE id = ?";

		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {

			ps.setString(1, id.toString());

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