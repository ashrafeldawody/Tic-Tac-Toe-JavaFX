/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package client.controllers;

import client.App;
import client.models.Server;
import client.models.Player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.model.impl.ToastTypes;

/**
 * FXML Controller class
 *
 * @author ashra
 */
public class LoginWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Server.connect("127.0.0.1", 5050);
    }

    @FXML
    private void login(ActionEvent ae) {
        Player.login(usernameField.getText(), passwordField.getText());
    }

    @FXML
    private void switchRegister(ActionEvent ae) throws IOException {
        App.setRoot("RegisterWindow");
    }

    @FXML
    private void close(MouseEvent ae) {
        Platform.exit();
    }
}
