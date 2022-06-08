package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    private final SimpleIntegerProperty customerID = new SimpleIntegerProperty();
    private final SimpleStringProperty customerName = new SimpleStringProperty();
    private final SimpleStringProperty address = new SimpleStringProperty();
    private final SimpleStringProperty postalCode = new SimpleStringProperty();
    private final SimpleStringProperty phone = new SimpleStringProperty();
    private final SimpleObjectProperty<Country> country = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Division> division = new SimpleObjectProperty<>();

    public Customer() {
    }

    public Customer(Integer id, String name, String address, String postalCode, String phone, Country country, Division division) {
        setCustomerID(id);
        setCustomerName(name);
        setAddress(address);
        setPostalCode(postalCode);
        setPhone(phone);
        setCountry(country);
        setDivision(division);
    }

    public Customer(Integer id) {
        setCustomerID(id);
    }

    public Integer getCustomerID() {
        return customerID.get();
    }

    public SimpleIntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getCustomerName() {
        return customerName.get();
    }

    public SimpleStringProperty customerNameProperty() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPostalCode(){
        return postalCode.get();
    }

    public SimpleStringProperty postalCodeProperty() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode.set(postalCode);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public Country getCountry() {
        return country.get();
    }

    public SimpleObjectProperty<Country> countryProperty() {
        return country;
    }

    public void setCountry(Country country) {
        this.country.set(country);
    }

    public Division getDivision() {
        return division.get();
    }

    public SimpleObjectProperty<Division> divisionProperty() {
        return division;
    }

    public void setDivision(Division division) {
        this.division.set(division);
    }
}
