/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.models;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.pomo.toasterfx.ToastBarToasterService;
import org.pomo.toasterfx.model.impl.ToastTypes;

/**
 *
 * @author ashra
 */
public class Helpers {
    public static void showDialog(Alert.AlertType type,String title,String content,Boolean exit){
        Platform.runLater(()->{
            Alert a = new Alert(type);
            a.setTitle(title);
            a.setHeaderText(title);
            a.setResizable(true);
            a.setContentText(content);
            a.showAndWait();
            if(exit) Platform.exit();
        });
    }
        public static void displayTray(String title,String text,ToastTypes type) {
            ToastBarToasterService service = new ToastBarToasterService();
            service.initialize();
            service.bomb(title,text, type);
    }
}
