package services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//main login for the sql server 

public abstract class BaseService {
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL Driver loaded!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected static final String DB_URL = "jdbc:mysql://localhost:3306/choreconnectmvp";
	protected static final String DB_USERNAME = "root";
	protected static final String DB_PASSWORD = "admin";

	protected Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
	}
}
