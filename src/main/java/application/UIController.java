package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

public class UIController {

    @FXML private TextField siteField, usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;
    @FXML private TextArea savedPasswordsArea;
    @FXML private ListView<String> passwordList;

    private static HashMap<String, String> passwordStorage = new HashMap<>();

    // Handles user login
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

    // Handles user registration
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

    // Saves password to storage
    @FXML
    private void savePassword() {
        String site = siteField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (site.isEmpty() || username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please fill all fields!", Alert.AlertType.ERROR);
            return;
        }

        // Encrypt password before storing
        String encryptedPassword = encryptPassword(password);

        if (DatabaseHelper.savePassword(username, site, encryptedPassword)) {
            showAlert("Success", "Password saved successfully!", Alert.AlertType.INFORMATION);
            passwordStorage.put(site + " | " + username, encryptedPassword);
            loadSavedPasswords();
            clearFields();
        } else {
            showAlert("Error", "Failed to save password!", Alert.AlertType.ERROR);
        }
    }

    // Switches to the dashboard after login
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

    // Generates a secure random password
    @FXML
    private void generatePassword() {
        String generatedPassword = generateSecurePassword(12);
        passwordField.setText(generatedPassword);
    }

    // Initializes UI and loads saved passwords
    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            if (savedPasswordsArea != null) {
                loadSavedPasswords();
            }
        });
    }

    // Loads saved passwords into the ListView
    @FXML
    private void loadSavedPasswords() {
        passwordList.getItems().clear();

        if (passwordStorage.isEmpty()) {
            statusLabel.setText("No saved passwords found.");
        } else {
            passwordList.getItems().addAll(passwordStorage.keySet());
            statusLabel.setText("Saved passwords loaded.");
        }
    }

    // Displays selected password details
    @FXML
    private void displaySelectedPassword() {
        String selectedEntry = passwordList.getSelectionModel().getSelectedItem();
        if (selectedEntry != null && passwordStorage.containsKey(selectedEntry)) {
            savedPasswordsArea.setText("Site: " + selectedEntry.split(" | ")[0] +
                    "\nUsername: " + selectedEntry.split(" | ")[1] +
                    "\nPassword: " + passwordStorage.get(selectedEntry));
        }
    }

    // Generates a secure password
    private String generateSecurePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

    // Encrypts password using SHA-256
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

    // Displays alert messages
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Clears input fields after saving password
    private void clearFields() {
        siteField.clear();
        usernameField.clear();
        passwordField.clear();
    }
}
