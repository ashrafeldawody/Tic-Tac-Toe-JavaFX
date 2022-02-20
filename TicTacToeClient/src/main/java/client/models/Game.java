/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.models;

import client.App;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author ashraf
 */
public class Game {
    public static Game currentGame;
    public int id;
    public Player me;
    public Player opponent;
    public String turn;
    public Date date;


    public Game(int _id, Player p1, Player p2, Date d) {
        id = _id;
        me = p1;
        opponent = p2;
        date = d;
    }

    public Game(Player p2) {
        me = Player.player;
        opponent = p2;
    }

    public static void rejectGameRequest() {
        Server.sendRequest(JSONRequests.playReject().toString());
    }

    public static void endCurrentGame() {
        if (Game.currentGame != null) Game.currentGame = null;
        ResponseHandler.tempOpponentUsername = null;
    }

    public void startGame() {
        App.setRoot("GameWindow");
    }

    public void sendGameRequest() {
        Server.sendRequest(JSONRequests.playRequest(opponent.username).toString());
    }

    public static void acceptGameRequest() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Record ?");
        alert.setContentText("Do you want to record this game?");
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("no", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);
        alert.showAndWait().ifPresent(type -> {
            if (type == okButton) {
                Server.sendRequest(JSONRequests.playAccept("yes").toString());
            } else {
                Server.sendRequest(JSONRequests.playAccept("no").toString());
            }
        });

    }

    public void play(int index) {
        Server.sendRequest(JSONRequests.play(index).toString());
    }

    public void sendMessage(String message) {
        Server.sendRequest(JSONRequests.messageSend(message).toString());
    }

    public Boolean isMyTurn() {

        if (me.move.equalsIgnoreCase(turn)) {
            return true;
        }
        return false;
    }
}
