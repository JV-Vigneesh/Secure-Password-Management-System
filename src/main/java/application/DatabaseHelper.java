package application;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:password_manager.db";
    public DatabaseHelper() {
        initializeDatabase();
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            if (conn != null) {
                System.out.println("Connected to the database!");
            }
            String userTable = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE, "
                    + "password TEXT)";

            String passwordTable = "CREATE TABLE IF NOT EXISTS passwords ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT, "
                    + "site TEXT, "
                    + "encrypted_password TEXT)";

            stmt.execute(userTable);
            stmt.execute(passwordTable);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }


    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, PasswordManager.encryptPassword(password));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return PasswordManager.verifyPassword(password, rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean savePassword(String username, String site, String password) {
        String sql = "INSERT INTO passwords (username, site, encrypted_password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, site);
            pstmt.setString(3, PasswordManager.encryptPassword(password));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static String getSavedPasswords(String username) {
        StringBuilder passwords = new StringBuilder();
        String sql = "SELECT site, encrypted_password FROM passwords WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                passwords.append("Site: ").append(rs.getString("site")).append("\n")
                        .append("Encrypted Password: ").append(rs.getString("encrypted_password")).append("\n\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords.toString();
    }

}
