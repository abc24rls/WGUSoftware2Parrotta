import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SQLDatabase;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("View_Controller/Login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Appointment System - Login");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) throws SQLException {
        SQLDatabase.connect();

        //Load Language Bundle
        ResourceBundle rb = ResourceBundle.getBundle("resources/Languages", Locale.getDefault());
        if (Locale.getDefault() == Locale.FRANCE) {
                System.out.println(rb.getString("hello") + " " + rb.getString("world"));
                }
        else System.out.println("hello world");

        launch(args);

        SQLDatabase.disconnect();
    }
}
