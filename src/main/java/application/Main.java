package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setTitle("Secure Password Manager - Login");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to load login.fxml!");
        }
    }

    public static void main(String[] args) {
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.initializeDatabase();
        launch(args);
    }

}
