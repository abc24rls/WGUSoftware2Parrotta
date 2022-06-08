package Model;

import utils.SQLDatabase;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDB {

    // Login Attempt
    public static User login(String username, String password) {
        User currentUser = null;

        try {
            String query = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setString(1,username);
            statement.setString(2,password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                currentUser = new User(rs.getInt("user_id"));
                currentUser.setUsername(rs.getString("user_Name"));
                currentUser.setUserId(rs.getInt("user_id"));
            }

            statement.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentUser;
    }
}
