package View_Controller;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewAppointmentsController implements Initializable {

    private Parent scene;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> customerId;
    @FXML private TableColumn<Customer, String> customerName;
    @FXML private TableColumn<Customer, String> address;
    @FXML private TableColumn<Customer, String> postalCode;
    @FXML private TableColumn<Customer, String> phone;
    @FXML private TableColumn<Customer, String> country;
    @FXML private TableColumn<Customer, String> division;
    @FXML private Label CustomerLabel;
    @FXML private TableView<Appointment> aptTable;
    @FXML private TableColumn<Appointment, Integer> aptID;
    @FXML private TableColumn<Appointment, String> aptTitle;
    @FXML private TableColumn<Appointment, String> aptDescription;
    @FXML private TableColumn<Appointment, String> aptLocation;
    @FXML private TableColumn<Appointment, String> aptContact;
    @FXML private TableColumn<Appointment, String> aptType;
    @FXML private TableColumn<Appointment, String> aptStart;
    @FXML private TableColumn<Appointment, String> aptEnd;
    @FXML private TableColumn<Appointment, Integer> user;

    @FXML private RadioButton weekly;
    @FXML private RadioButton monthly;
    @FXML private Label Label;

    @FXML Label userZone;
    @FXML Label currentUserLabel;
    private Customer selectedCustomer;
    private Appointment selectedAppointment;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        currentUserLabel.setText("Currrent User: " + (currentUser).toString());
    }

    private void loadCustomerInfo (Customer customer){
        Integer id = customer.getCustomerID();
        CustomerLabel.setText((customer.getCustomerName()));

        if (weekly.isSelected()){
            aptTable.getItems().clear();
            aptTable.getItems().addAll(AppointmentDB.getApptsWeekly(id));

        }
        else if (monthly.isSelected()){
            aptTable.getItems().clear();
            aptTable.getItems().addAll(AppointmentDB.getMonthlyAppt(id));
        }

        aptTable.refresh();
    }

    public void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        customerTable.getSelectionModel().select(selectedCustomer);
        loadCustomerInfo(selectedCustomer);
        }

    @FXML void handleCustomerSelection() {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        loadCustomerInfo(selectedCustomer);
    }

    public void handleBackButton(ActionEvent event) {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    @FXML void handleAddAppointments(ActionEvent event) {
        try {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText("You must first select a customer");
                Optional<ButtonType> outcome = alert.showAndWait();
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAppointment.fxml"));
                scene = loader.load();
                AddAppointmentController controller = loader.getController();
                controller.setCustomer(selectedCustomer);
                controller.setCurrentUser(currentUser);
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (IOException e){
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    public void handleAddCustomer(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddCustomer.fxml"));
        scene = loader.load();
        AddCustomerController controller = loader.getController();
        controller.setCurrentUser(currentUser);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML void handleEditAppointments(ActionEvent event) {
        try {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            selectedAppointment = aptTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null || selectedAppointment == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText("You must select an appointment to edit");
                Optional<ButtonType> outcome = alert.showAndWait();
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditAppointment.fxml"));
                scene = loader.load();
                EditAppointmentController controller = loader.getController();
                controller.setSelectedAppointment(selectedAppointment, selectedCustomer);
                controller.setCurrentUser(currentUser);
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (IOException e){
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    @FXML void handleEditCustomer(ActionEvent event) {
        try {
            selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setHeaderText("You must first select a customer");
                Optional<ButtonType> outcome = alert.showAndWait();
            } else {
                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("EditCustomers.fxml"));
                scene = loader.load();
                EditCustomersController controller = loader.getController();
                controller.setCustomer(selectedCustomer);
                controller.setCurrentUser(currentUser);
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (IOException e){
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    @FXML void radioButtonChanges() {
        if(weekly.isSelected()){
            Label.setText("Weekly Appointments");
        }
        if(monthly.isSelected()){
            Label.setText("Monthly Appointments");
        }
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("You must select a customer to view their appointments");
            Optional<ButtonType> outcome = alert.showAndWait();
            }
        else{loadCustomerInfo(selectedCustomer);}
    }

    @FXML void handleDeleteAppointment() throws IOException {
        selectedAppointment = aptTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("An appointment must be selected to delete");
            Optional<ButtonType> outcome = alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRM DELETE");
            alert.setHeaderText("Are you sure would you like to delete " + selectedAppointment.getAptTitle() + "?");
            Optional<ButtonType> outcome = alert.showAndWait();
            if (outcome.get() == ButtonType.OK) {
                AppointmentDB.deleteAppointment(selectedAppointment.getAptID());
                handleCustomerSelection();
            }
        }
    }

    @FXML void handleDeleteCustomer() {
        selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("You must first select a customer");
            Optional<ButtonType> outcome = alert.showAndWait();
        }else if (AppointmentDB.checkApts(selectedCustomer.getCustomerID())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("The customer you are trying to delete has future appointments. Delete all future appointments to remove this customer");
            Optional<ButtonType> outcome = alert.showAndWait();
        } else {
            Alert alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("CONFIRM DELETE");
            alert.setHeaderText("Are you sure would you like to delete " + selectedCustomer.getCustomerName() + "?");
            Optional<ButtonType> outcome = alert.showAndWait();
            if(outcome.get()==ButtonType.OK){
                CustomerDB.deleteCustomer(selectedCustomer.getCustomerID());
                AppointmentDB.deletePastApt(selectedCustomer.getCustomerID());
                //Refresh Customer Table
                customerTable.setItems(CustomerDB.getAllCustomers());
            }
        }
    }

    public void handleReports(ActionEvent event) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Reports.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ReportsController controller = loader.getController();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        country.setCellValueFactory(new PropertyValueFactory<>("country"));
        division.setCellValueFactory(new PropertyValueFactory<>("division"));
        aptID.setCellValueFactory(new PropertyValueFactory<>("aptID"));
        aptTitle.setCellValueFactory(new PropertyValueFactory<>("aptTitle"));
        aptDescription.setCellValueFactory(new PropertyValueFactory<>("aptDescription"));
        aptStart.setCellValueFactory(new PropertyValueFactory<>("aptStart"));
        aptEnd.setCellValueFactory(new PropertyValueFactory<>("aptEnd"));
        aptLocation.setCellValueFactory(new PropertyValueFactory<>("aptLocation"));
        aptType.setCellValueFactory(new PropertyValueFactory<>("aptType"));
        aptContact.setCellValueFactory(new PropertyValueFactory<>("aptContact"));
        user.setCellValueFactory(new PropertyValueFactory<>("user"));

        customerTable.setItems(CustomerDB.getAllCustomers());
        Locale locale = Locale.getDefault();
        String zone = TimeZone.getDefault().getDisplayName();
        userZone.setText("Current Zone: " + zone );
    }
}