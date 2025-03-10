package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:password_manager.db";

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:password_manager.db");
    }


    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS passwords (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, site TEXT, encrypted_password TEXT NOT NULL)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String username, String password) {
        // Trim the password before hashing
        password = password.trim();

        String hashedPassword = PasswordManager.hashPassword(password);
        System.out.println("Storing Hashed Password for user: " + hashedPassword);

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authenticateUser(String username, String password) {
        password = password.trim();  // Trim input password

        String sql = "SELECT password FROM users WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password").trim();
                System.out.println("Stored Hashed Password for user: " + storedHashedPassword);

                boolean match = PasswordManager.verifyPassword(password, storedHashedPassword);
                System.out.println("Password verification result: " + match);
                return match;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean savePassword(String username, String site, String password, String encryptedPassword) {
        String sql = "INSERT INTO passwords (username, site, encrypted_password) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, site);
            pstmt.setString(3, encryptedPassword); // Encrypt the password before storing
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getSavedPasswords(String username) {
        List<String> passwords = new ArrayList<>();
        String sql = "SELECT site, encrypted_password FROM passwords WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String site = rs.getString("site");
                String decryptedPassword = PasswordManager.decryptPassword(rs.getString("encrypted_password"));
                passwords.add(site + " | " + decryptedPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    public static boolean deletePassword(String username, String site) {
        String sql = "DELETE FROM passwords WHERE username = ? AND site = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, site);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updatePassword(String username, String site, String newUsername, String newPassword) {
        String sql = "UPDATE passwords SET username = ?, encrypted_password = ? WHERE username = ? AND site = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newUsername);
            pstmt.setString(2, PasswordManager.encryptPassword(newPassword));
            pstmt.setString(3, username);
            pstmt.setString(4, site);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
