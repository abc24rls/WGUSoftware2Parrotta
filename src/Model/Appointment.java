package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Appointment {
    private final SimpleIntegerProperty aptID = new SimpleIntegerProperty();
    private final SimpleStringProperty aptTitle = new SimpleStringProperty();
    private final SimpleStringProperty aptDescription = new SimpleStringProperty();
    private final SimpleStringProperty aptLocation = new SimpleStringProperty();
    private final SimpleObjectProperty<Contact> aptContact = new SimpleObjectProperty<>();
    private final SimpleStringProperty aptType = new SimpleStringProperty();
    private final SimpleObjectProperty<Customer> aptCustomer = new SimpleObjectProperty();
    private final SimpleObjectProperty<Timestamp> aptStart = new SimpleObjectProperty<>();
    private final SimpleObjectProperty<Timestamp> aptEnd = new SimpleObjectProperty<>();
    private final SimpleIntegerProperty user = new SimpleIntegerProperty();

    public Appointment() {}

    public Appointment(Integer id, String title, Integer user) {
        setAptID(id);
        setAptTitle(title);
        setUser(user);
    }

    public Appointment(Integer id, String title, String description, String location, String type, Customer customer, Timestamp start, Timestamp end, Contact contact, Integer user) {
        setAptID(id);
        setAptTitle(title);
        setAptDescription(description);
        setAptLocation(location);
        setAptType(type);
        setAptCustomer(customer);
        setAptStart(start);
        setAptEnd(end);
        setAptContact(contact);
        setUser(user);
    }

    public Integer getAptID() {
        return aptID.get();
    }

    public SimpleIntegerProperty aptIDProperty() {
        return aptID;
    }

    public void setAptID(int aptID) {
        this.aptID.set(aptID);
    }

    public String getAptTitle() {
        return aptTitle.get();
    }

    public SimpleStringProperty aptTitleProperty() {
        return aptTitle;
    }

    public void setAptTitle(String aptTitle) {
        this.aptTitle.set(aptTitle);
    }

    public String getAptDescription() {
        return aptDescription.get();
    }

    public SimpleStringProperty aptDescriptionProperty() {
        return aptDescription;
    }

    public void setAptDescription(String aptDescription) {
        this.aptDescription.set(aptDescription);
    }

    public String getAptLocation() {
        return aptLocation.get();
    }

    public SimpleStringProperty aptLocationProperty() {
        return aptLocation;
    }

    public void setAptLocation(String aptLocation) {
        this.aptLocation.set(aptLocation);
    }

    public Contact getAptContact() {
        return aptContact.get();
    }

    public SimpleObjectProperty<Contact> aptContactProperty() {
        return aptContact;
    }

    public void setAptContact(Contact aptContact) {
        this.aptContact.set(aptContact);
    }

    public String getAptType() {
        return aptType.get();
    }

    public SimpleStringProperty aptTypeProperty() {
        return aptType;
    }

    public void setAptType(String aptType) {
        this.aptType.set(aptType);
    }

    public Customer getAptCustomer() {
        return aptCustomer.get();
    }

    public SimpleObjectProperty<Customer> aptCustomerProperty() {
        return aptCustomer;
    }

    public void setAptCustomer(Customer aptCustomer) {
        this.aptCustomer.set(aptCustomer);
    }

    public LocalDateTime getAptStart() {
        return aptStart.get().toLocalDateTime();
    }

    public SimpleObjectProperty<Timestamp> aptStartProperty() {
        return aptStart;
    }

    public void setAptStart(Timestamp aptStart) {
        this.aptStart.set(aptStart);
    }

    public Timestamp getAptEnd() {
        return aptEnd.get();
    }

    public SimpleObjectProperty<Timestamp> aptEndProperty() {
        return aptEnd;
    }

    public void setAptEnd(Timestamp aptEnd) {
        this.aptEnd.set(aptEnd);
    }

    public Integer getUser() {
        return user.get();
    }

    public SimpleIntegerProperty userProperty() {
        return user;
    }

    public void setUser(Integer user) {
        this.user.set(user);
    }

    public LocalDate getAptStartDate() {
        return aptStart.get().toLocalDateTime().toLocalDate();
    }

    public String getAptStartTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    return aptStart.get().toLocalDateTime().format(formatter);
    }

    public LocalDate getAptEndDate() {
        return aptEnd.get().toLocalDateTime().toLocalDate();
    }

    public String getAptEndTime() {
        return aptEnd.get().toLocalDateTime().toLocalTime().toString();
    }
}
