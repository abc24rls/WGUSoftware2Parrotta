package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class AddAppointmentController {
    public Customer selectedCustomer;
    @FXML private TextField CustomerField;
    @FXML private ComboBox Time;
    @FXML private ComboBox<Contact> Contact;
    @FXML private DatePicker Date;
    @FXML private ComboBox<String> Locations;
    @FXML private TextField ApptType;
    @FXML private TextField Title;
    @FXML private TextField Description;

    private final ObservableList<String> times = FXCollections.observableArrayList("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
    private final ObservableList<String> locations = FXCollections.observableArrayList( "MS Teams", "Zoom","Conference Room 1", "Conference Room 2", "Theater");

    private String error;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        CustomerField.setText(selectedCustomer.getCustomerName());
        Time.setItems(times);
        ObservableList<Contact> contacts = FXCollections.observableList(ContactDB.getContacts());
        Contact.setItems(contacts);
        Locations.setItems(locations);
    }

    public void handleSaveButton(ActionEvent event) throws IOException, SQLException {
        Integer addAppointmentID = AppointmentDB.assignAppointmentID();
        String addTitle = Title.getText();
        String addDescription = Description.getText();
        String addLocation = Locations.getSelectionModel().getSelectedItem();
        String addType = ApptType.getText();
        LocalDate aptDate = Date.getValue();
        String aptTime = Time.getSelectionModel().getSelectedItem().toString();
        Integer addContactID = Contact.getSelectionModel().getSelectedItem().getContactId();
        LocalDateTime formattedTime = AppointmentDB.convertToDateAndTime(aptDate, aptTime);
        Integer addCustomer = selectedCustomer.getCustomerID();
        Integer addUserId = currentUser.getUserId();

        //Validation check
        if (!checkDate(aptDate) || !checkTime(aptTime) || !checkContact(addContactID) || !checkLocation(addLocation) || !checkType()) {
            System.out.println(error);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct the indicated error:");
            alert.setContentText(error);
            Optional<ButtonType> outcome = alert.showAndWait();
            return;
        }
        if (AppointmentDB.checkOverlappingAppointment(addCustomer, formattedTime)){
            return;
        }
        else {
            AppointmentDB.addAppointment(addAppointmentID, addTitle, addDescription, addLocation, addType, formattedTime, addCustomer, addContactID, addUserId);

            ((Node)(event.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAppointments.fxml"));
            Object scene = loader.load();
            ViewAppointmentsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            controller.setSelectedCustomer(selectedCustomer);
            }
        }

    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    private boolean checkType() {
        if(ApptType.getText().isEmpty()) {
            error = "Add an Appointment Type";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLocation(String aptLocation) {
        if(aptLocation == null) {
            error = "Choose a location";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkContact(int aptContact) {
        if(aptContact == -1) {
            error = "Choose a contact";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTime(String aptTime) {
        if(aptTime == null) {
            error = "Choose a time";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkDate(LocalDate aptDate) {
        if(aptDate == null) {
            error = "Choose a date";
            return false;
        }
        if (aptDate.isBefore(LocalDate.now()))
        {
            error = "Choose a future date";
            return false;
        }
        if(aptDate.getDayOfWeek() == DayOfWeek.SATURDAY || aptDate.getDayOfWeek() == DayOfWeek.SUNDAY ){
            error = "Choose a weekday";
            return false;
        }
        else {
            return true;
        }
    }
}
