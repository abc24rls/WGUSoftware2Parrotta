package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utils.SQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerDB {

    private static final ObservableList<Customer> allCustomers = FXCollections.observableArrayList();

    private static String error;

    public static Customer getCustomer(Integer id) {
        try {
            String query = "SELECT * FROM customers WHERE customer_Id = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Customer customer = new Customer();
                customer.setCustomerName(rs.getString("customer_Name"));
                statement.close();
                return customer;
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public static ObservableList<Customer> getAllCustomers() {
        allCustomers.clear();
        try {
            Statement statement = SQLDatabase.getConnection().createStatement();
            String query = "SELECT c.customer_id, c.customer_Name, c.address, c.postal_Code,c.phone, co.country, d.division, co.country_id, d.division_id"
                + " FROM customers c "
                + " INNER JOIN first_level_divisions d ON c.division_id = d.division_id"
                + " INNER JOIN countries co ON co.country_Id = d.country_Id";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Country country = new Country(
                    rs.getInt("country_id"),
                    rs.getString("country")
                );

                Division division = new Division(
                    rs.getInt("division_id"),
                    rs.getString("division")
                );

                Customer customer = new Customer(
                    rs.getInt("customer_id"),
                    rs.getString("customer_name"),
                    rs.getString("address"),
                    rs.getString("postal_Code"),
                    rs.getString("phone"),
                    country,
                    division
                );
                allCustomers.add(customer);
            }

            statement.close();
            return allCustomers;

        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public static void addCustomer(String customerName, String address, String postalCode, String phone, Integer divisionId, Integer userId) {
        {
            try {
                Integer customerID = assignCustomerID();

                String query = "INSERT INTO customers (Customer_ID,Customer_Name, Address, Postal_Code, Phone, Division_id, Create_Date, Created_By, Last_Update, Last_Updated_By) VALUES (?,?,?,?,?,?,NOW(),?,NOW(),?)";
                PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
                statement.setInt(1,customerID);
                statement.setString(2, customerName);
                statement.setString(3,address);
                statement.setString(4,postalCode);
                statement.setString(5,phone);
                statement.setInt(6,divisionId);
                statement.setInt(7, userId);
                statement.setInt(8, userId);
                statement.executeUpdate();
                System.out.println("Customer Added");

                } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static void editCustomer(Integer customerID, String customerName, String address, String postalCode, String phone, Integer divisionId, Integer userId) {
        {

            try {
                String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_id = ?, Last_Update = NOW(), Last_Updated_By = ? WHERE customer_id = ?";
                PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
                statement.setString(1, customerName);
                statement.setString(2,address);
                statement.setString(3,postalCode);
                statement.setString(4,phone);
                statement.setInt(5,divisionId);
                statement.setInt(6, userId);
                statement.setInt(7,customerID);
                statement.executeUpdate();
                System.out.println("Update Successful");

            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static Integer assignCustomerID(){
        Integer customerID = 1;
        try {
            Statement statement = SQLDatabase.getConnection().createStatement();
            String query = "SELECT customer_Id FROM customers ORDER BY customer_Id";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                if (rs.getInt("customer_Id") == customerID) {
                    customerID++;
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return customerID;
    }

    public static void deleteCustomer(Integer customerID){
        try {
            String queryDelete = "DELETE FROM customers WHERE customer_Id=?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(queryDelete);
            statement.setInt(1,customerID);
            statement.executeUpdate();
            System.out.println("Successfully deleted customer");

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static Boolean validateCustomer(String customerName, String address, String postalCode, String phone, Country country, Division division){
        error = "";
        if(!validName(customerName) || !validAddress(address) || !validPostalCode(postalCode) || !validPhone(phone)|| !validCountry(country) || !validDivision(division)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText("Correct the indicated error:");
            alert.setContentText(error);
            Optional<ButtonType> outcome = alert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }

    public static List<String> getDivisions (String country) {
        List<String> results = new ArrayList<String>();
        try {
            String query = "SELECT * FROM  First_level_divisions INNER JOIN Countries USING (Country_ID) WHERE Country = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setString(1,country);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                results.add(rs.getString("division"));
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return results;
    }

    private static boolean validPostalCode(String postalCode) {
        if(postalCode.isEmpty()) {
            error = "Enter a Postal Code";
            return false;
        }
        else{
            return true;
        }
    }

    private static boolean validCountry(Country country) {
        if(country == null) {
            error = "Select a Country";
            return false;
        }
        else{
            return true;
        }
    }

    private static boolean validDivision(Division division) {
        if(division == null) {
            error = "Select a Division";
            return false;
        }
        else{
            return true;
        }
    }

    private static boolean validAddress(String address) {
        if(address.isEmpty()) {
            error = "Enter an Address";
            return false;
        }
        else{
            return true;
        }
    }

    private static boolean validPhone(String phone) {
        if(phone.isEmpty()) {
            error = "Enter a Phone Number";
            return false;
        }
        else{
            return true;
        }
    }

    private static boolean validName(String name) {
        if(name.isEmpty()) {
            error = "Enter a Customer Name";
            return false;
        }
        else{
            return true;
        }
    }
}
