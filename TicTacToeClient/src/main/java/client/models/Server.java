/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.control.Alert;

public abstract class Server {

    private static BufferedReader inputStream;
    private static PrintStream outputStream;
    private static Socket socket;

    public static void connect(String ipAddress, int portNumber) {
        try {
            socket = new Socket(InetAddress.getByName(ipAddress), portNumber);
            inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputStream = new PrintStream(socket.getOutputStream());

        } catch (IOException e) {
            Helpers.showDialog(Alert.AlertType.ERROR, "Error", "Failed to connect to server", true);
        }
        startListening();
    }

    public static void sendRequest(String line) {
        outputStream.println(line);
    }


    public static void startListening() {
        new Thread(() ->
        {
            try {
                while (socket != null && !(socket.isClosed())) {
                    String str = inputStream.readLine();
                    ResponseHandler.handleResponse(str);
                }
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }


}
