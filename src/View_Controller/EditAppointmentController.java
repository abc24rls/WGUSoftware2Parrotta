package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditAppointmentController implements Initializable {
    public Customer selectedCustomer;
    public Appointment selectedAppointment;
    @FXML private TextField CustomerField;
    @FXML private ComboBox Time;
    @FXML private ComboBox<Contact> Contact;
    @FXML private DatePicker Date;
    @FXML private ComboBox Locations;
    @FXML private TextField AptType;
    @FXML private TextField Description;
    @FXML private TextField Title;

    private final ObservableList<String> times = FXCollections.observableArrayList("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
    private final ObservableList<String> locations = FXCollections.observableArrayList( "MS Teams", "Zoom","Conference Room 1", "Conference Room 2", "Theater");

    private String error;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void setCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }

    public void setSelectedAppointment(Appointment selectedAppointment, Customer selectedCustomer) {
        this.selectedAppointment = selectedAppointment;
        this.selectedCustomer = selectedCustomer;
        CustomerField.setText(selectedCustomer.getCustomerName());
        Date.setValue(selectedAppointment.getAptStartDate());
        Time.setValue(selectedAppointment.getAptStartTime());
        Contact.getSelectionModel().select(selectedAppointment.getAptContact());
        Locations.setValue(selectedAppointment.getAptLocation());
        AptType.setText(selectedAppointment.getAptType());
        Title.setText(selectedAppointment.getAptTitle());
        Description.setText(selectedAppointment.getAptDescription());
    }

    public void handleSaveButton(ActionEvent event) throws IOException, SQLException {
        LocalDate aptDate = Date.getValue();
        String aptTime = Time.getSelectionModel().getSelectedItem().toString();
        Contact addContact = Contact.getSelectionModel().getSelectedItem();
        Integer aptLocation = Locations.getSelectionModel().getSelectedIndex();
        LocalDateTime formattedTime = AppointmentDB.convertToDateAndTime(aptDate, Time.getSelectionModel().getSelectedItem().toString());
        if (!checkDate(aptDate) || !checkTime(aptTime) || !checkContact(addContact) || !checkLocation(aptLocation) || !checkType()) {
            System.out.println(error);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct the indicated error:");
            alert.setContentText(error);
            Optional<ButtonType> outcome = alert.showAndWait();
        }
        else if (AppointmentDB.checkOverlappingAppointment(selectedCustomer.getCustomerID(), formattedTime)){
            return;
        }
        else {
            Integer aptID = selectedAppointment.getAptID();
            String addTitle = Title.getText();
            String addDescription = Description.getText();
            String addLocation = Locations.getSelectionModel().getSelectedItem().toString();
            String addType = AptType.getText();
            Integer addCustomer = selectedCustomer.getCustomerID();
            Integer addUserId = currentUser.getUserId();
            AppointmentDB.updateAppointment(aptID, addTitle, addDescription, addLocation, addType, formattedTime, addContact.getContactId(), addCustomer, addUserId);

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
        if(AptType.getText().isEmpty()) {
            error = "Please add an Appointment Type";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkLocation(int aptLocation) {
        if(aptLocation == -1) {
            error = "Please choose a location";
            return false;
        } else {
            return true;
        }
    }

    private boolean checkContact(Model.Contact aptContact) {
        if(aptContact == null) {
            error = "Please choose a contact";
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
            error = "Please choose a date";
            return false;
        }
        if (aptDate.isBefore(LocalDate.now()))
        {
            error = "Please choose a future date";
            return false;
        }
        if(aptDate.getDayOfWeek() == DayOfWeek.SATURDAY || aptDate.getDayOfWeek() == DayOfWeek.SUNDAY ){
            error = "Please choose a weekday";
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        java.util.List<Contact> list = ContactDB.getContacts();
        ObservableList<Contact> contacts = FXCollections.observableList(list);
        Contact.setItems(contacts);
        Locations.setItems(locations);
        Time.setItems(times);
    }
}