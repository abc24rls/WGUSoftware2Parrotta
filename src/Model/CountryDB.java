package Model;

import utils.SQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryDB {

    public static List<Country> getCountries () {
        List<Country> results = new ArrayList<>();
        try {
            String query = "SELECT * FROM countries";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Country country = new Country(
                        rs.getInt("country_id"),
                        rs.getString("country")
                );
            results.add(country);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return results;
    }

}
