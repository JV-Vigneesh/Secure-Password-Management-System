package application;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

public class DashboardController {

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private ListView<String> passwordList;

    @FXML
    private void generatePassword() {
        // Placeholder for password generation logic
        newPasswordField.setText("Generated@123");
    }

    @FXML
    private void savePassword() {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or Password cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        passwordList.getItems().add(username + ": " + password);
        newUsernameField.clear();
        newPasswordField.clear();
    }

    @FXML
    private void loadSavedPasswords() {
        // Placeholder: Load saved passwords from a real database or storage.
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
