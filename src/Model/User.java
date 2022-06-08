package Model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class User {
    private final SimpleStringProperty username = new SimpleStringProperty();
    private final SimpleIntegerProperty userId = new SimpleIntegerProperty();
    private final SimpleStringProperty password = new SimpleStringProperty();

    public User(String username,Integer userId){
        setUsername(username);
        setUserId(userId);
    }

    public User(String username){
        setUsername(username);
    }

    public User(Integer userId){
        setUserId(userId);
    }

    public String getUsername(){
        return username.get();
    }

    public SimpleStringProperty usernameProperty(){
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public Integer getUserId(){
        return userId.get();
    }

    public SimpleIntegerProperty userIdProperty(){
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId.set(userId);
    }

    public String getPassword(){
        return password.get();
    }

    public SimpleStringProperty passwordProperty(){
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    @Override
    public String toString() {
        return username.get();
    }
}
