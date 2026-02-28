package services;
import models.Log;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class LogService {

	
	
	static {
	    try {
	        Class.forName("com.mysql.cj.jdbc.Driver");
	        System.out.println("MySQL Driver loaded!");
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	private String dbURL = "jdbc:mysql://localhost:3306/loggydb";
    private String dbUsername = "root";
    private String dbPassword = "admin";


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL, dbUsername, dbPassword);
    }

    // sql for adding a new log into the DB
    public void createLog(Log log) throws SQLException {
        String sql = "INSERT INTO logs (id, title, content) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, log.getId());
            stmt.setString(2, log.getTitle());
            stmt.setString(3, log.getContent());
            stmt.executeUpdate();
        }
    }

    // part where we read all the logs from the db 
    public List<Log> getAllLogs() throws SQLException {
        List<Log> logs = new ArrayList<>();
        String sql = "SELECT * FROM logs ORDER BY timestamp DESC";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Log log = new Log();
                log.setId(rs.getString("id"));
                log.setTitle(rs.getString("title"));
                log.setContent(rs.getString("content"));
                log.setTimestamp(rs.getTimestamp("timestamp").toInstant());
                logs.add(log);
            }
        }
        return logs;
    }

    // Updating Log 
    public void updateLog(Log log) throws SQLException {
        String sql = "UPDATE logs SET title=?, content=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, log.getTitle());
            stmt.setString(2, log.getContent());
            stmt.setString(3, log.getId());
            stmt.executeUpdate();
        }
    }

    // delete log from the DB
    public void deleteLog(String id) throws SQLException {
        String sql = "DELETE FROM logs WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        }
    }
}