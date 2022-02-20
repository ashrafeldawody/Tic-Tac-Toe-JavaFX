/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Eagle
 */
class ClientHandler extends Thread {

    BufferedReader bufferReader;
    PrintStream printStream;
    public ClientHandler(Socket socket) {
        try {
            bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    public void run() {
        try {
            String inputLine;
            while (!Thread.interrupted() && (inputLine = bufferReader.readLine()) != null) {
                System.out.println(inputLine);
                JSONObject parsedRequest = new JSONObject(inputLine.trim());
                String action = (String) parsedRequest.get("action");
                if (action.equals("login")) {
                    handleLoginRequest((String) parsedRequest.get("username"), (String) parsedRequest.get("password"));
                } else if (action.equals("register")) {
                    handleRegisterRequest((String) parsedRequest.get("username"), (String) parsedRequest.get("password"));
                }else{
                    printStream.println(Helpers.getStatusObject("Unknown Request"));
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private Boolean checkAlreadyLoggedIn(String username){
        for (Client client: Client.onlineClients) {
            if(client.getUsername().equalsIgnoreCase(username))
                return true;
            }
        return false;
    }
    private void removeClientByUsername(String username){
        for (Client c: Client.onlineClients) {
            if(c.getUsername().equalsIgnoreCase(username))
                Client.onlineClients.remove(c);
        }
    }

    private void handleLoginRequest(String username, String password) {
        if(checkAlreadyLoggedIn(username)){
            printStream.println(JSONRequests.login(false,"This username is already logged in from other client",null));
            return;
        }
        Player _player = Player.login(username,password);
        if (_player != null) {
            this.interrupt();
            new Client(_player.getUsername(),_player.getPoints(),bufferReader, printStream);
            printStream.println(JSONRequests.login(true,"Logged In",_player));
        } else {
            printStream.println(JSONRequests.login(false,"Username or password is wrong",null));
        }
    }
    private void handleRegisterRequest(String username, String password) {
        if (Player.checkUsername(username)) {
            printStream.println(JSONRequests.register(false,"The username already registered"));
        } else {
            Player.register(username,password);
            printStream.println(JSONRequests.register(true,"Your Account has been registered"));
        }
    }


}
