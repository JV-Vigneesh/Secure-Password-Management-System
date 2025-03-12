package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:password_manager.db";

    public static void initializeDatabase() {
        try (Connection conn = getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            String userTable = "CREATE TABLE IF NOT EXISTS loginusers ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "username TEXT UNIQUE, "
                    + "password TEXT)";

            stmt.execute(userTable);
            System.out.println("✅ Database initialized successfully!");
        } catch (SQLException e) {
            System.err.println("⚠ Database initialization failed: " + e.getMessage());
        }
    }

    // ✅ **Register user (Stores hashed password)**
    public static boolean registerUser(String username, String password) {
        String hashedPassword = PasswordManager.hashPassword(password);

        String sql = "INSERT INTO loginusers (username, password) VALUES (?, ?)";
        try (Connection conn = getConnection(DB_URL);
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

    // ✅ **Authenticate user**
    public static boolean authenticateUser(String username, String password) {
        String sql = "SELECT password FROM loginusers WHERE username = ?";
        try (Connection conn = getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password").trim();
                System.out.println("Stored Hashed Password: " + storedHashedPassword);

                boolean match = PasswordManager.verifyPassword(password, storedHashedPassword);
                System.out.println("Password verification result: " + match);
                return match;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ **Create user-specific table for passwords**
    public static void createUserTable(String username) {
        String sql = "CREATE TABLE IF NOT EXISTS " + username + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "site TEXT, "
                + "username TEXT, "
                + "encrypted_password TEXT, "
                + "UNIQUE(username, site))"; // Prevent duplicate username for the same site

        try (Connection conn = getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Table created for user: " + username);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ **Save Password**
    public static boolean savePassword(String loggedInUser, String site, String username, String encryptedPassword) {
        createUserTable(loggedInUser); // Ensure table exists

        // Check if username already exists for the site
        String checkSql = "SELECT COUNT(*) FROM " + loggedInUser + " WHERE username = ? AND site = ?";
        try (Connection conn = getConnection(DB_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, site);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("❌ Duplicate username found for the same website!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Insert new password
        String sql = "INSERT INTO " + loggedInUser + " (site, username, encrypted_password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, site);
            pstmt.setString(2, username);
            pstmt.setString(3, encryptedPassword);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ **Get Saved Passwords**
    public static List<PasswordEntry> getSavedPasswords(String loggedInUser) {
        createUserTable(loggedInUser); // Ensure table exists

        List<PasswordEntry> passwords = new ArrayList<>();
        String sql = "SELECT site, username, encrypted_password FROM " + loggedInUser;

        try (Connection conn = getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String site = rs.getString("site");
                String storedUsername = rs.getString("username");
                String encryptedPassword = rs.getString("encrypted_password");

                String decryptedPassword = PasswordManager.decryptPassword(encryptedPassword);

                passwords.add(new PasswordEntry(site, storedUsername, decryptedPassword));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    public static boolean updatePassword(String loggedInUser, String site, String oldUsername, String newUsername, String newPassword) {
        String tableName = loggedInUser; // ✅ User-specific table
        String sql = "UPDATE " + tableName + " SET username = ?, encrypted_password = ? WHERE username = ? AND site = ?";

        try (Connection conn = getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String encryptedPassword = PasswordManager.encryptPassword(newPassword);

            pstmt.setString(1, newUsername);  // Update with new username
            pstmt.setString(2, encryptedPassword);  // Update with new encrypted password
            pstmt.setString(3, oldUsername);  // Match the original username
            pstmt.setString(4, site);  // Match the site name

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;  // If at least one row is updated, return true

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public static boolean deletePassword(String loggedInUser, String site, String username) {
        String tableName = loggedInUser; // ✅ User-specific table
        String sql = "DELETE FROM " + tableName + " WHERE username = ? AND site = ?";

        try (Connection conn = getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, site);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
