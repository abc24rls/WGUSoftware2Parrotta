package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Contact {

    private final SimpleStringProperty contact = new SimpleStringProperty();

    private final SimpleIntegerProperty contactId = new SimpleIntegerProperty();

    public Contact() {}

    public Contact(Integer contactId) {
        setContactId(contactId);
    }

    public Contact(Integer contactId, String contact) {
        setContact(contact);
        setContactId(contactId);
    }

    public String getContact() {
        return contact.get();
    }

    public SimpleStringProperty contactProperty() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }

    public Integer getContactId() {
        return contactId.get();
    }

    public SimpleIntegerProperty contactIdProperty() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId.set(contactId);
    }

    @Override
    public String toString() {
        return contact.get();
    }
}
