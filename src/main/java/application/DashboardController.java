package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class DashboardController {
    @FXML private TableView<PasswordEntry> passwordTable;
    @FXML private TableColumn<PasswordEntry, String> siteColumn;
    @FXML private TableColumn<PasswordEntry, String> usernameColumn;
    @FXML private TableColumn<PasswordEntry, String> passwordColumn;
    private ObservableList<PasswordEntry> passwordData = FXCollections.observableArrayList();

    @FXML private TextField siteField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    private String loggedInUser;

    @FXML
    public void initialize() {
        siteColumn.setCellValueFactory(new PropertyValueFactory<>("site"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    public void setLoggedInUser(String username) {
        this.loggedInUser = username;
        loadSavedPasswords();
    }

    @FXML
    private void savePassword() {
        if (loggedInUser == null || loggedInUser.isEmpty()) {
            showAlert("Error", "User not logged in!", Alert.AlertType.ERROR);
            return;
        }

        String site = siteField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (site.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Site, username, and password cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        String encryptedPassword = PasswordManager.encryptPassword(password);

        if (DatabaseHelper.savePassword(loggedInUser, site, username, encryptedPassword)) {
            showAlert("Success", "Password saved successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords();
        } else {
            showAlert("Error", "Failed to save password!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void loadSavedPasswords() {
        if (loggedInUser == null || loggedInUser.isEmpty()) {
            showAlert("Error", "User not logged in!", Alert.AlertType.ERROR);
            return;
        }

        List<PasswordEntry> passwordEntries = DatabaseHelper.getSavedPasswords(loggedInUser);
        passwordData.setAll(passwordEntries);
        passwordTable.setItems(passwordData);
    }

    @FXML
    private void generatePassword() {
        String generatedPassword = PasswordManager.generateSecurePassword(12); // Generate 12-character secure password
        passwordField.setText(generatedPassword); // Set it in the PasswordField
    }

    @FXML
    private void deleteSelectedPassword() {
        PasswordEntry selected = passwordTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a password to delete!", Alert.AlertType.ERROR);
            return;
        }

        String loggedInUser = AuthManager.getLoggedInUser(); // ✅ Get correct user table

        boolean isDeleted = DatabaseHelper.deletePassword(loggedInUser, selected.getSite(), selected.getUsername());

        if (isDeleted) {
            showAlert("Success", "Password deleted successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords();  // Refresh table after deletion
        } else {
            showAlert("Error", "Failed to delete password!", Alert.AlertType.ERROR);
        }
    }



    @FXML
    private void editSelectedPassword() {
        PasswordEntry selected = passwordTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a password to edit!", Alert.AlertType.ERROR);
            return;
        }

        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();

        if (newUsername.isEmpty() || newPassword.isEmpty()) {
            showAlert("Error", "Username and Password cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        String loggedInUser = AuthManager.getLoggedInUser(); // ✅ Get correct user table

        boolean isUpdated = DatabaseHelper.updatePassword(loggedInUser, selected.getSite(), selected.getUsername(), newUsername, newPassword);

        if (isUpdated) {
            showAlert("Success", "Password updated successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords();  // Refresh table after update
        } else {
            showAlert("Error", "Failed to update password!", Alert.AlertType.ERROR);
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
