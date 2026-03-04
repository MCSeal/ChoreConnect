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

 
    protected String dbURL = "jdbc:mysql://localhost:3306/choreconnectmvp";
    protected String dbUsername = "root";
    protected String dbPassword = "admin";

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL, dbUsername, dbPassword);
    }
}
