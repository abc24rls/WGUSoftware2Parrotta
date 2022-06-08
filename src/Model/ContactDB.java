package Model;


import utils.SQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDB {

    public static List<Contact> getContacts () {
        List<Contact> results = new ArrayList<>();
        try {
            String query = "SELECT * FROM contacts ORDER BY contact_name ASC";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Contact contact= new Contact(
                    rs.getInt("contact_id"),
                    rs.getString("contact_name")
                );
                results.add(contact);
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return results;
    }
}
