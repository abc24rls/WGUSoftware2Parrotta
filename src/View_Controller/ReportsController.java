package View_Controller;

import Model.AppointmentDB;
import Model.User;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;
import utils.Log;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML TreeTableView aptMatTable;
    @FXML TreeTableColumn matMonth;
    @FXML TreeTableView schedTable;
    @FXML TreeTableColumn schedContact;
    @FXML TreeTableView modifiedApts;
    @FXML TreeTableColumn users;

    private User currentUser;

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        aptMatTable.setShowRoot(false);
        TreeItem<String> matRoot = new TreeItem<>("Appointment Types");
        HashMap<String, List<String>> matMap = AppointmentDB.getMatTable();
        for (String contact : matMap.keySet()) {
            TreeItem<String> parent = new TreeItem<>(contact);
            matRoot.getChildren().add(parent);
            matMap.get(contact).forEach(detail -> {
                TreeItem<String> child = new TreeItem<>(detail);
                parent.getChildren().add(child);
            });
        }

        aptMatTable.setRoot(matRoot);
        matMonth.setCellValueFactory(
            (Object param) -> {
                TreeTableColumn.CellDataFeatures item = (TreeTableColumn.CellDataFeatures) param;
                return new ReadOnlyStringWrapper(item.getValue().getValue().toString());
            }
        );

        schedTable.setShowRoot(false);
        TreeItem<String> schedRoot = new TreeItem<>("Contact Schedules");
        HashMap<String, List<String>> schedMap = AppointmentDB.getSchedule();
        for (String contact : schedMap.keySet()) {
            TreeItem<String> parent = new TreeItem<>(contact);
            schedRoot.getChildren().add(parent);
            schedMap.get(contact).forEach(detail -> {
                TreeItem<String> child = new TreeItem<>(detail);
                parent.getChildren().add(child);
            });
        }

        schedTable.setRoot(schedRoot);
        schedContact.setCellValueFactory(
            (Object param) -> {
                TreeTableColumn.CellDataFeatures item = (TreeTableColumn.CellDataFeatures) param;
                return new ReadOnlyStringWrapper(item.getValue().getValue().toString());
            }
        );

        modifiedApts.setShowRoot(false);
        TreeItem<String> modRoot = new TreeItem<>("Modified Appointments");
        HashMap<String, List<String>> modMap = AppointmentDB.getModApts();
        for (String user : modMap.keySet()) {
            TreeItem<String> parent = new TreeItem<>(user);
            modRoot.getChildren().add(parent);
            modMap.get(user).forEach(detail -> {
                TreeItem<String> child = new TreeItem<>(detail);
                parent.getChildren().add(child);
            });
        }

        modifiedApts.setRoot(modRoot);
        users.setCellValueFactory(
            (Object param) -> {
                TreeTableColumn.CellDataFeatures item = (TreeTableColumn.CellDataFeatures) param;
                return new ReadOnlyStringWrapper(item.getValue().getValue().toString());
            }
        );
    }

    public void handleBackButton(ActionEvent event) throws NullPointerException, IOException {
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }
}
