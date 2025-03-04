package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UIController {

    @FXML private TextField siteField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private ListView<String> passwordList;
    @FXML private TextArea savedPasswordsArea;

    private static HashMap<String, String[]> passwordStorage = new HashMap<>();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (DatabaseHelper.authenticateUser(username, password)) {
            showAlert("Login Successful", "Welcome " + username, Alert.AlertType.INFORMATION);
            switchToDashboard();
        } else {
            showAlert("Login Failed", "Invalid credentials", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (DatabaseHelper.registerUser(username, password)) {
            showAlert("Registration Successful", "You can now log in", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void savePassword() {
        String site = siteField.getText().trim();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (site.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required!", Alert.AlertType.ERROR);
            return;
        }

        String encryptedPassword = encryptPassword(password);
        passwordStorage.put(site, new String[]{username, encryptedPassword});

        loadSavedPasswords();
        showAlert("Success", "Password saved successfully!", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void editPassword() {
        String selectedEntry = passwordList.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            showAlert("Error", "Select a password to edit.", Alert.AlertType.ERROR);
            return;
        }

        String site = selectedEntry.split(" \\| ")[0];

        if (!passwordStorage.containsKey(site)) {
            showAlert("Error", "Entry not found!", Alert.AlertType.ERROR);
            return;
        }

        // Populate fields with existing data for editing
        siteField.setText(site);
        usernameField.setText(passwordStorage.get(site)[0]);
        passwordField.setText(passwordStorage.get(site)[1]);  // Display the encrypted password

        // Remove old entry so it can be updated
        passwordStorage.remove(site);
        loadSavedPasswords();
    }

    @FXML
    private void deletePassword() {
        String selectedEntry = passwordList.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            showAlert("Error", "Select a password to delete.", Alert.AlertType.ERROR);
            return;
        }

        String site = selectedEntry.split(" \\| ")[0];
        passwordStorage.remove(site);
        loadSavedPasswords();
        showAlert("Success", "Password deleted successfully!", Alert.AlertType.INFORMATION);
    }

    private void switchToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));

            Stage stage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Failed to load dashboard", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void generatePassword() {
        String generatedPassword = generateSecurePassword(12);
        passwordField.setText(generatedPassword);
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::loadSavedPasswords);
    }

    @FXML
    private void loadSavedPasswords() {
        passwordList.getItems().clear();

        if (passwordStorage.isEmpty()) {
            statusLabel.setText("No saved passwords found.");
        } else {
            for (Map.Entry<String, String[]> entry : passwordStorage.entrySet()) {
                String site = entry.getKey();
                String username = entry.getValue()[0];
                String password = entry.getValue()[1];  // Encrypted password
                passwordList.getItems().add(site + " | " + username + " | " + password);
            }
            statusLabel.setText("Saved passwords loaded.");
        }
    }

    private String generateSecurePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
