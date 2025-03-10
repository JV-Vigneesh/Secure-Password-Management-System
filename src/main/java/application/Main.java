package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        DatabaseHelper.initializeDatabase(); // Ensure DB is set up before UI loads
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        String password = "user";
        String hashedPassword = PasswordManager.hashPassword(password);
        System.out.println("Hashed Password: " + hashedPassword);

        boolean match = PasswordManager.verifyPassword(password, hashedPassword);
        System.out.println("Password Match: " + match);  // Should print true
    }
}


