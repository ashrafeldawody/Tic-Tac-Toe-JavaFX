/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package client.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.App;
import client.models.Helpers;
import client.models.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author ashra
 */
public class RegisterWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    @FXML
    private void register(ActionEvent ae){
        if(!passwordField.getText().equals(passwordConfirmField.getText())){
            Helpers.showDialog(Alert.AlertType.ERROR,"Failed","Password Confirmation doesn't match",false);
            return;
        }
        if(passwordField.getText().length() < 4){
            Helpers.showDialog(Alert.AlertType.ERROR,"Failed","Password is too short",false);
            return;
        }
        if(usernameField.getText().length() < 4){
            Helpers.showDialog(Alert.AlertType.ERROR,"Failed","Username is too short",false);
            return;
        }
        Player.register(usernameField.getText(),passwordField.getText());
    }
    @FXML
    private void close(MouseEvent ae){
        Platform.exit();
    }
    @FXML
    private void back(MouseEvent me) throws IOException {
        App.setRoot("LoginWindow");
    }

}
