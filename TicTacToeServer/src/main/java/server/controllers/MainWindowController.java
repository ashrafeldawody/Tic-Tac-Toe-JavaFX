/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package server.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.application.Platform;
import server.models.Player;
import server.models.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author ashraf
 */
public class MainWindowController implements Initializable {

    @FXML
    private TextField portField;
    @FXML
    private Button startServerBtn;
    @FXML
    private Button stopServerBtn;
    @FXML
    private Label statusText;
    @FXML
    private TableView<Player> playersTable;
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<Player, String> usernameCol = new TableColumn<>("Player");
        usernameCol.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());

        TableColumn<Player, Number> pointsCol = new TableColumn<>("Points");
        pointsCol.setCellValueFactory(cellData -> cellData.getValue().pointsProperty());

        TableColumn<Player, Boolean> onlineCol = new TableColumn<>("Status");
        onlineCol.setCellValueFactory(cellData -> cellData.getValue().onlineProperty());
        playersTable.getColumns().addAll(usernameCol,pointsCol,onlineCol);

    }
    public void loadTableData(){
        try {
            Player.getAll();


            playersTable.setItems(Player.playersList);
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Failed");
            a.setHeaderText("Connection Failed");
            a.setResizable(true);
            a.setContentText("Connection to Database Failed");
            a.showAndWait();
            System.exit(0);
        }


    }
    public void startServer(ActionEvent ae) {
        if (portField.getText().isEmpty() || !Pattern.matches(
                "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$",
                portField.getText())) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Failed");
            a.setHeaderText("Server Failed to start");
            a.setResizable(true);
            a.setContentText("The port number is not valid");
            a.showAndWait();
            return;
        }
        int port = Integer.parseInt(portField.getText());
        Server.createServer(port);
        stopServerBtn.setVisible(true);
        startServerBtn.setVisible(false);
        statusText.setTextFill(Color.GREEN);
        statusText.setText("Online");
        portField.setDisable(true);
        loadTableData();
    }
    private void setServerStopped(){
        Server.stop();
        stopServerBtn.setVisible(false);
        startServerBtn.setVisible(true);
        statusText.setTextFill(Color.RED);
        statusText.setText("Offline");
        portField.setDisable(false);
    }
    public void stopServer(ActionEvent ae) {
        setServerStopped();
    }
    public void exit(ActionEvent ae) {
        setServerStopped();
        System.exit(0);
    }
}
