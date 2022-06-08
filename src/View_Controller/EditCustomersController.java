package View_Controller;

import Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditCustomersController implements Initializable {
    @FXML TextField CustomerName;
    @FXML TextField Phone;
    @FXML TextField Address;
    @FXML ComboBox Country;
    @FXML TextField PostalCode;
    @FXML ComboBox<Division> Division;

    public Customer selectedCustomer;
    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void onCountrySelected() {
        Country currentCountry = (Country) Country.getValue();
        List<Division> list = DivisionDB.getDivisions(currentCountry.getCountryId());
        ObservableList<Division> divisions = FXCollections.observableList(list);
        Division.setItems(divisions);
        Division.setValue(null);
    }

    public void handleBackButton(ActionEvent event) throws NullPointerException, IOException {
        Stage stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void handleUpdateButton(ActionEvent event) throws IOException {
        Integer customerID = selectedCustomer.getCustomerID();
        String addCustomerName = CustomerName.getText();
        String addAddress = Address.getText();
        String addPostalCode = PostalCode.getText();
        String addPhone = Phone.getText();
        Division addDivision = Division.getValue();
        Country addCountry = (Country) Country.getValue();
        Integer addUserId = currentUser.getUserId();

        if (CustomerDB.validateCustomer(addCustomerName, addAddress, addPostalCode, addPhone, addCountry, addDivision)){
            CustomerDB.editCustomer(customerID, addCustomerName, addAddress, addPostalCode, addPhone, addDivision.getDivisionId(), addUserId);
            ((Node)(event.getSource())).getScene().getWindow().hide();
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAppointments.fxml"));
            Object scene = loader.load();
            ViewAppointmentsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        java.util.List<Country> list = CountryDB.getCountries();
        ObservableList<Country> countries = FXCollections.observableList(list);
        Country.setItems(countries);
    }

    public void setCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
        CustomerName.setText(selectedCustomer.getCustomerName());
        Phone.setText(selectedCustomer.getPhone());
        Address.setText(selectedCustomer.getAddress());
        PostalCode.setText(selectedCustomer.getPostalCode());
        Country.setValue(selectedCustomer.getCountry());
        Division.getSelectionModel().select(selectedCustomer.getDivision());


        Country currentCountry = (Country) Country.getValue();
        List<Division> list = DivisionDB.getDivisions(currentCountry.getCountryId());
        ObservableList<Division> divisions = FXCollections.observableList(list);
        Division.setItems(divisions);
    }
}

