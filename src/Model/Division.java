package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Division {

    private final SimpleStringProperty division = new SimpleStringProperty();

    private final SimpleIntegerProperty divisionId = new SimpleIntegerProperty();

    public Division() {}

    public Division(Integer divisionId, String division) {
        setDivision(division);
        setDivisionId(divisionId);
    }

    public String getDivision() {
        return division.get();
    }

    public SimpleStringProperty divisionProperty() {
        return division;
    }

    public void setDivision(String division) {
        this.division.set(division);
    }

    public Integer getDivisionId() {
        return divisionId.get();
    }

    public SimpleIntegerProperty divisionIdProperty() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId.set(divisionId);
    }

    @Override
    public String toString() {
        return division.get();
    }
}
