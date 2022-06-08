package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import utils.SQLDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class AppointmentDB {
    public static Appointment get15MinuteApt() {
        Appointment apt;
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneID = ZoneId.systemDefault();
        ZonedDateTime zdt = now.atZone((zoneID));
        LocalDateTime ldt = zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime ldt15 = ldt.plusMinutes(15);

        try {
            String query = "SELECT * FROM appointments WHERE start BETWEEN ? AND ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setTimestamp(1, Timestamp.valueOf(ldt));
            statement.setTimestamp(2, Timestamp.valueOf(ldt15));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                apt = new Appointment();
                apt.setAptID(rs.getInt("appointment_Id"));
                apt.setAptStart(rs.getTimestamp("start"));
                return apt;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static ObservableList<Appointment> getMonthlyAppt(Integer id) {
        ObservableList<Appointment> monthlyAppt = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate now = LocalDate.now();
        LocalDate oneMonth = LocalDate.now().plusMonths(1);

        try {
            String query = "SELECT a.appointment_id, a.title, a.description, a.location, a.type, a.customer_id, a.start, a.end, cn.contact_id, cn.contact_name, a.user_id FROM appointments a"
                    + " INNER JOIN customers c ON a.customer_id = c.customer_id"
                    + " INNER JOIN contacts cn ON a.contact_id = cn.contact_id"
                    + " WHERE a.customer_ID = ? AND start BETWEEN ? AND ? ORDER BY start ASC";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,id);
            statement.setDate(2, Date.valueOf(now));
            statement.setDate(3,Date.valueOf(oneMonth));
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("contact_name")
                );
                Customer customer = new Customer(
                        rs.getInt("customer_id")
                );
                appointment = new Appointment(
                        rs.getInt("appointment_Id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        customer,
                        rs.getTimestamp("start"),
                        rs.getTimestamp("end"),
                        contact,
                        rs.getInt("user_id"));
                monthlyAppt.add(appointment);
            }
            statement.close();
            return monthlyAppt;
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public static HashMap<String, List<String>> getMatTable() {
        HashMap<String, List<String>> matMap = new HashMap<>();

        try {
            String query = "SELECT type, count(type) as Count, monthname(start) AS Month FROM appointments GROUP BY start, type";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String typeCount = rs.getInt("count") + " - " + rs.getString("type");
                String month = rs.getString("month");

                if (matMap.containsKey(month)){
                    matMap.get(month).add(typeCount);
                }
                else{
                    List<String> list = new ArrayList<>();
                    list.add(typeCount);
                    matMap.put(month, list);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return matMap;
    }

    public static HashMap<String, List<String>> getSchedule() {
        HashMap<String, List<String>> schedMap = new HashMap<>();

        try {
            String query = "SELECT a.appointment_id, a.title, a.type, a.description, DATE_FORMAT(a.start, '%M %e %y' ) as date, TIME_FORMAT(a.start, '%k:%i') as start, TIME_FORMAT(a.end, '%k:%i') as end, a.customer_id, c.contact_name FROM appointments a " +
                " INNER JOIN contacts c on c.contact_Id = a.contact_id" +
                " WHERE Start > now() GROUP BY a.contact_ID, a.start";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String contact = rs.getString("contact_name");
                String details ="AptID: " +
                    rs.getInt("appointment_Id") + ", Title: " +
                    rs.getString("title") + ", Type: " +
                    rs.getString("type") + ", Description: " +
                    rs.getString("description") + ", " +
                    rs.getString("date") + " at " +
                    rs.getString("start") + "-" +
                    rs.getString("end") + ", CustomerID: " +
                    rs.getInt("customer_id");
                if (schedMap.containsKey(contact)){
                    schedMap.get(contact).add(details);
                }
                else{
                    List<String> list = new ArrayList<>();
                    list.add(details);
                    schedMap.put(contact, list);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return schedMap;
    }

    public static HashMap<String, List<String>> getModApts() {
        HashMap<String, List<String>> modMap = new HashMap<>();

        try {
            String query = "SELECT user_id, appointment_id, title, DATE_FORMAT(last_update, '%M %e %y' ) as date, TIME_FORMAT(last_update, '%k:%i') as time FROM client_schedule.appointments WHERE create_date != last_update GROUP BY User_ID";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                String user = "UserID " + rs.getString("user_Id");
                String aptdetails = "AptID: " +
                    rs.getInt("appointment_Id") + ", " +
                    rs.getString("title") + ", " +
                    rs.getString("date") + " at "+
                    rs.getString("time");
                if (modMap.containsKey(user)){
                    modMap.get(user).add(aptdetails);
                }
                else {
                    List<String> list = new ArrayList<>();
                    list.add(aptdetails);
                    modMap.put(user, list);
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return modMap;
    }

    public static ObservableList<Appointment> getApptsWeekly(Integer id) {
        ObservableList<Appointment> weeklyAppts = FXCollections.observableArrayList();
        Appointment appointment;
        LocalDate now = LocalDate.now();
        LocalDate oneWeek = LocalDate.now().plusDays(7);

        try {
            String query = "SELECT a.appointment_id, a.title, a.description, a.location, a.type, a.customer_id, a.start, a.end, cn.contact_id, cn.contact_name, a.user_id FROM appointments a"
                + " INNER JOIN customers c ON a.customer_id = c.customer_id"
                + " INNER JOIN contacts cn ON a.contact_id = cn.contact_id"
                + " WHERE a.customer_ID =? AND start BETWEEN ? AND ? ORDER BY start ASC";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,id);
            statement.setDate(2, Date.valueOf(now));
            statement.setDate(3,Date.valueOf(oneWeek));
            ResultSet rs = statement.executeQuery();

            while(rs.next()) {
                Contact contact = new Contact(
                        rs.getInt("contact_id"),
                        rs.getString("contact_name")
                );
                Customer customer = new Customer(
                        rs.getInt("customer_id")
                );
                appointment = new Appointment(
                        rs.getInt("appointment_Id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getString("type"),
                        customer,
                        rs.getTimestamp("start"),
                        rs.getTimestamp("end"),
                        contact,
                        rs.getInt("user_id"));
                weeklyAppts.add(appointment);
            }
            statement.close();
            return weeklyAppts;
        }
        catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public static LocalDateTime convertToDateAndTime(LocalDate Date, String Time){
        String str = Date + " " + Time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(str, formatter);
        System.out.println(dateTime);
        return dateTime;
    }

    public static Integer assignAppointmentID(){
        Integer appointmentID = 1;
        try {
            Statement statement = SQLDatabase.getConnection().createStatement();
            String query = "SELECT appointment_Id FROM appointments ORDER BY appointment_Id";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()) {
                if (rs.getInt("appointment_Id") == appointmentID) {
                    appointmentID++;
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return appointmentID;
    }

    public static void addAppointment(Integer appointmentID, String title, String description, String location,String type, LocalDateTime apptStart, Integer customerID, Integer contactID, Integer userID){

        ZonedDateTime ldtZoned = apptStart.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = utcStart.plusHours(1);
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();

        try {
            String query = "INSERT INTO appointments (appointment_Id, title, description, location, type, start, end, create_Date, created_By, last_Update,last_Updated_By, customer_ID, user_ID, contact_ID) " +
                    "           VALUES (?,?,?,?,?,?,?,NOW(),?,NOW(),?,?,?,?)";

            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,appointmentID);
            statement.setString(2, title);
            statement.setString(3, description);
            statement.setString(4,location);
            statement.setString(5, type);
            statement.setTimestamp(6, Timestamp.valueOf(startConvert));
            statement.setTimestamp(7, Timestamp.valueOf(endConvert));
            statement.setInt(8, userID);
            statement.setInt(9, userID);
            statement.setInt(10, customerID);
            statement.setInt(11, userID);
            statement.setInt(12, contactID);
            statement.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static boolean checkOverlappingAppointment(Integer customer, LocalDateTime time) throws SQLException {
        ZonedDateTime ldtZoned = time.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = utcStart.plusHours(1);
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();

        String query = "SELECT * FROM appointments WHERE customer_Id = ? AND (start BETWEEN ? AND ? OR end BETWEEN ? AND ?)";
        PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
        statement.setInt(1,customer);
        statement.setTimestamp(2,Timestamp.valueOf(startConvert));
        statement.setTimestamp(3, Timestamp.valueOf(endConvert));
        statement.setTimestamp(4,Timestamp.valueOf(startConvert));
        statement.setTimestamp(5, Timestamp.valueOf(endConvert));
        ResultSet rs = statement.executeQuery();
        int overlappingAppts = 0;

        while (rs.next()) {
            overlappingAppts++;
        }
        if (overlappingAppts > 0) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Appointment Overlap");
            alert.setHeaderText("This appointment overlaps with another appointment, select a different time or date");
            Optional<ButtonType> outcome = alert.showAndWait();
            return true;
        }
        else {
            return false;
        }
    }

    public static void updateAppointment(Integer appointmentID, String title, String description, String location,String type, LocalDateTime apptStart, Integer contactId, Integer customerId, Integer userID){
        ZonedDateTime ldtZoned = apptStart.atZone(ZoneId.systemDefault());
        ZonedDateTime utcStart = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEnd = utcStart.plusHours(1);
        LocalDateTime startConvert = utcStart.toLocalDateTime();
        LocalDateTime endConvert = utcEnd.toLocalDateTime();

        try {
            String query = "UPDATE appointments SET title = ?, description = ?, location = ?, type = ?, start = ?, end = ?, last_update = NOW(), last_updated_by = ?, customer_id = ?, user_id = ?, contact_id = ? WHERE appointment_id = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setString(1, title);
            statement.setString(2,description);
            statement.setString(3, location);
            statement.setString(4, type);
            statement.setTimestamp(5, Timestamp.valueOf(startConvert));
            statement.setTimestamp(6, Timestamp.valueOf(endConvert));
            statement.setInt(7, userID);
            statement.setInt(8, customerId);
            statement.setInt(9,userID);
            statement.setInt(10, contactId);
            statement.setInt(11,appointmentID);
            statement.executeUpdate();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void deleteAppointment(Integer apptID){
        try {
            String query = "DELETE FROM appointments WHERE appointment_Id = ?";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,apptID);
            statement.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static boolean checkApts(Integer customerID){
        try {
            String query = "Select *  FROM appointments WHERE customer_Id = ? AND end > NOW()";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1,customerID);
            ResultSet rs = statement.executeQuery();
            return rs.next();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return true;
    }
    public static void deletePastApt(Integer customerID){
        try {
            String query = "DELETE FROM appointments WHERE customer_id = ? AND end < NOW()";
            PreparedStatement statement = SQLDatabase.getConnection().prepareStatement(query);
            statement.setInt(1, customerID);
            statement.execute();

        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}