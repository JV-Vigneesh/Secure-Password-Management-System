package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    @FXML
    private void savePassword() {
        String site = siteField.getText();
        String password = passwordField.getText();

        if (site.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Site and Password cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        // Encrypt password before saving
        String encryptedPassword = PasswordManager.encryptPassword(password);

        if (DatabaseHelper.savePassword(currentUsername, site, password, encryptedPassword)) {
            showAlert("Success", "Password saved successfully!", Alert.AlertType.INFORMATION);
            loadSavedPasswords(); // Refresh password list
        } else {
            showAlert("Error", "Failed to save password!", Alert.AlertType.ERROR);
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
        passwordList.getItems().clear();
        List<String> savedPasswords = DatabaseHelper.getSavedPasswords(currentUsername);
        passwordList.getItems().addAll(savedPasswords);
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
