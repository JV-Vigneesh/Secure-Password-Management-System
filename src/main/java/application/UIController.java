package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UIController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (AuthManager.authenticateUser(username, password)) {
            AuthManager.setLoggedInUser(username); // ✅ Store the logged-in user
            showAlert("Success", "Login successful!", Alert.AlertType.INFORMATION);
            switchToDashboard();
        } else {
            showAlert("Error", "Invalid username or password!", Alert.AlertType.ERROR);
        }
    }



    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username and password cannot be empty", Alert.AlertType.ERROR);
            return;
        }

        if (AuthManager.registerUser(username, password)) {
            showAlert("Success", "Registration successful!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Username already exists!", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void switchToRegister(ActionEvent event) {
        switchScene(event, "/register.fxml");  // Ensure correct path
    }


    @FXML
    private void switchToLogin(ActionEvent event) {
        switchScene(event, "/login.fxml");  // Ensure correct path
    }


    private void switchScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dashboard.fxml"));
            Parent root = loader.load();

            DashboardController controller = loader.getController();
            controller.setLoggedInUser(AuthManager.getLoggedInUser()); // ✅ Pass logged-in user

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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
