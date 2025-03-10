package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {

    @FXML private TextField siteField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TableView<PasswordEntry> passwordTable;
    @FXML private TableColumn<PasswordEntry, String> siteColumn;
    @FXML private TableColumn<PasswordEntry, String> usernameColumn;
    @FXML private TableColumn<PasswordEntry, String> passwordColumn;

    private ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList();
    private String loggedInUser;

    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
        loadSavedPasswords();
    }

    @FXML
    private void generatePassword() {
        passwordField.setText(PasswordManager.generateSecurePassword(12));
    }

    public static boolean savePassword(String username, String site, String encryptedPassword) { // ✅ Fix function signature
        String sql = "INSERT INTO passwords (username, site, encrypted_password) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, site);
            pstmt.setString(3, encryptedPassword);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    private ListView<String> passwordList; // Ensure this matches FXML

    private String currentUsername; // Store logged-in user

    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }


    @FXML
    private void loadSavedPasswords() {
        String username = AuthManager.getLoggedInUser();
        if (username == null || username.isEmpty()) {
            showAlert("Error", "User not logged in!", Alert.AlertType.ERROR);
            return;
        }

        List<String> passwords = DatabaseHelper.getSavedPasswords(username);
        ObservableList<PasswordEntry> passwordEntries = FXCollections.observableArrayList();

        for (String entry : passwords) {
            String[] parts = entry.split(" \\| ");
            if (parts.length == 2) {
                passwordEntries.add(new PasswordEntry(parts[0], username, parts[1]));
            }
        }

        passwordTable.setItems(passwordEntries);
    }

    public static List<String> getSavedPasswords(String username) {
        List<String> passwords = new ArrayList<>();
        String sql = "SELECT site, encrypted_password FROM passwords WHERE username = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String site = rs.getString("site");
                String decryptedPassword = PasswordManager.decryptPassword(rs.getString("encrypted_password"));
                if (!decryptedPassword.equals("Decryption Error!")) { // Prevent corrupt passwords
                    passwords.add(site + " | " + decryptedPassword);
                } else {
                    passwords.add(site + " | (Error: Cannot decrypt)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }


    @FXML
    private void editSelectedPassword() {
        PasswordEntry selected = passwordTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a password to edit!", Alert.AlertType.ERROR);
            return;
        }

        siteField.setText(selected.getSite());
        usernameField.setText(selected.getUsername());
        passwordField.setText(selected.getPassword());

        if (DatabaseHelper.updatePassword(loggedInUser, selected.getSite(), selected.getUsername(), passwordField.getText())) {
            showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords(); // Refresh table
        } else {
            showAlert("Error", "Failed to update password!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void deleteSelectedPassword() {
        PasswordEntry selected = passwordTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a password to delete!", Alert.AlertType.ERROR);
            return;
        }

        if (DatabaseHelper.deletePassword(loggedInUser, selected.getSite())) {
            showAlert("Success", "Password deleted successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords(); // Refresh table
        } else {
            showAlert("Error", "Failed to delete password!", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
