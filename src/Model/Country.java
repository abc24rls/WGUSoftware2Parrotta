package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Country {

    private final SimpleStringProperty country = new SimpleStringProperty();

    private final SimpleIntegerProperty countryId = new SimpleIntegerProperty();

    public Country(){}

    public Country(Integer countryId, String country){
        setCountry(country);
        setCountryId(countryId);
    }

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public int getCountryId() {
        return countryId.get();
    }

    public SimpleIntegerProperty countryIdProperty() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId.set(countryId);
    }

    @Override
    public String toString() {
        return country.get();
    }
}