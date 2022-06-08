package View_Controller;

import Model.Appointment;
import Model.AppointmentDB;
import Model.User;
import Model.UserDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.Log;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {
    @FXML TextField username;
    @FXML TextField password;
    @FXML Label usernameLbl;
    @FXML Label Passwordlbl;
    @FXML Button Loginbtn;
    @FXML Button exitbtn;
    @FXML Label userZone;

    private String alertTitle;
    private String alertHeader;
    private String alertContext;

    public void loginClick(ActionEvent event) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();
        User validUser = UserDB.login(username, password);

        if(validUser != null){
            // Launch Main Screen
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAppointments.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            ViewAppointmentsController controller = loader.getController();
            controller.setCurrentUser(validUser);
            Log.log(username, true, "Login Attempt");
            stage.setScene(scene);
            stage.show();

            //Check for appointments
            Appointment appointment = AppointmentDB.get15MinuteApt();
            if (appointment != null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Appoinment Reminder");
                alert.setHeaderText("Appointment " + appointment.getAptID() + " at " + appointment.getAptStart()+ " begins within 15 minutes");
                Optional<ButtonType> outcome = alert.showAndWait();
            }
        }
        else{
            Log.log(username, false, "Login Attempt");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(alertTitle);
            alert.setHeaderText(alertHeader);
            alert.setContentText(alertContext);
            Optional<ButtonType> outcome = alert.showAndWait();
        }
    }

    @FXML public void exitButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Are you sure you want to close the application");
        Optional<ButtonType> outcome = alert.showAndWait();
        if(outcome.get()==ButtonType.OK){
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle rb) {
        //Locale locale = Locale.getDefault();
        Locale locale = Locale.FRANCE;
        String zone = TimeZone.getDefault().getDisplayName();
        userZone.setText("Current Zone: " + zone );

        rb = ResourceBundle.getBundle("resources/Languages", locale);
        usernameLbl.setText(rb.getString("Username"));
        Passwordlbl.setText(rb.getString("Password"));
        Loginbtn.setText(rb.getString("login"));
        alertTitle = rb.getString("alertTitle");
        alertHeader = rb.getString("alertHeader");
        alertContext = rb.getString("alertContext");
        exitbtn.setText(rb.getString("Exit"));
    }
}
