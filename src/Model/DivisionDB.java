package Model;


import utils.SQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DivisionDB {
    public static List<Division> getDivisions (Integer countryId) {
        List<Division> results = new ArrayList<>();
        try {
            String query = "SELECT * FROM first_level_divisions WHERE country_id = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,countryId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Division division= new Division(
                        rs.getInt("division_id"),
                        rs.getString("division")
                );
                results.add(division);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return results;
    }
}
