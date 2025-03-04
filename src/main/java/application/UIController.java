package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class UIController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel; // Optional: To show save status

    @FXML
    private TextArea savedPasswordsArea; // Ensure this is linked to FXML


    // Simulating user storage (Replace with actual database or file storage)
    private static HashMap<String, String> userDatabase = new HashMap<>();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
            showAlert("Login Successful!", "Welcome " + username, Alert.AlertType.INFORMATION);
            switchToDashboard();
        } else {
            showAlert("Login Failed", "Invalid username or password", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "Username and password cannot be empty", Alert.AlertType.WARNING);
            return;
        }

        if (userDatabase.containsKey(username)) {
            showAlert("Registration Failed", "Username already exists", Alert.AlertType.ERROR);
        } else {
            userDatabase.put(username, password);
            showAlert("Registration Successful", "You can now log in", Alert.AlertType.INFORMATION);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToDashboard() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("dashboard.fxml"));
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
        String generatedPassword = "P@ssw0rd"; // Replace with actual password generation logic
        passwordField.setText(generatedPassword);
    }

    @FXML
    private void savePassword() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            statusLabel.setText("No password to save!");
        } else {
            // Add logic to save password (e.g., store in HashTable)
            statusLabel.setText("Password saved successfully!");
        }
    }

    @FXML
    public void initialize() {
        System.out.println("UIController initialized!");
        Platform.runLater(() -> {
            if (savedPasswordsArea != null) {
                System.out.println("savedPasswordsArea is properly initialized.");
                loadSavedPasswords();
            } else {
                System.err.println("ERROR: savedPasswordsArea is NULL! Check FXML binding.");
            }
        });
    }



    @FXML
    private void loadSavedPasswords() {
        System.out.println("Loading saved passwords..."); // Debugging

        List<String> savedPasswords = List.of("ExamplePass1", "ExamplePass2");

        if (savedPasswords.isEmpty()) {
            statusLabel.setText("No saved passwords found.");
        } else {
            savedPasswordsArea.clear(); // Ensure it's empty before adding
            savedPasswords.forEach(password -> savedPasswordsArea.appendText(password + "\n"));
            statusLabel.setText("Saved passwords loaded.");
        }
    }

}
