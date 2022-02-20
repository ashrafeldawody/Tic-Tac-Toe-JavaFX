/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package client.controllers;

import client.App;
import client.models.*;

import java.net.URL;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * FXML Controller class
 *
 * @author ashraf
 */
public class GameWindowController implements Initializable {
    public static GameWindowController me;

    private Boolean GameOver = false;

    @FXML
    private GridPane gameGrid;
    @FXML
    private Button sendMessagebtn;
    @FXML
    private Button playAgainbtn;
    @FXML
    private Button backbtn;
    @FXML
    private Button field1;
    @FXML
    private Button field2;
    @FXML
    private Button field3;
    @FXML
    private Button field4;
    @FXML
    private Button field5;
    @FXML
    private Button field6;
    @FXML
    private Button field7;
    @FXML
    private Button field8;
    @FXML
    private Button field9;
    @FXML
    private Label myUsername;
    @FXML
    private Label myMove;
    @FXML
    private Label opponentUsername;
    @FXML
    private Label opponentMove;
    @FXML
    private Label headerLabel;
    @FXML
    private TextArea chatBox;
    @FXML
    private TextField chatTextField;

    public void initialize(URL url, ResourceBundle rb) {
        me = this;
        refreshTurn();
        refreshHeader();
        myUsername.setText(Game.currentGame.me.username);
        myMove.setText(Game.currentGame.me.move);

        opponentUsername.setText(Game.currentGame.opponent.username);
        opponentMove.setText(Game.currentGame.opponent.move);
    }
    public void refreshTurn(){
        if(Game.currentGame.isMyTurn()){
            myMove.setTextFill(Color.GREEN);
            opponentMove.setTextFill(Color.WHITE);
        }else{
            myMove.setTextFill(Color.WHITE);
            opponentMove.setTextFill(Color.GREEN);
        }
    }
    public void refreshHeader(){
        headerLabel.setVisible(true);
        if(Game.currentGame.isMyTurn()){
            headerLabel.setTextFill(Color.GREEN);
            headerLabel.setText("Your Turn");
        }else{
            headerLabel.setTextFill(Color.RED);
            headerLabel.setText("Opponent Turn");
        }
    }
    public void setMove(int index, String move) {
        refreshTurn();
        refreshHeader();
        new SoundPlayer(SoundPlayer.SOUND.PLAYER_ACTION_A).play();
        switch (index) {
            case 0:
                field1.setText(move);
                break;
            case 1:
                field2.setText(move);
                break;
            case 2:
                field3.setText(move);
                break;
            case 3:
                field4.setText(move);
                break;
            case 4:
                field5.setText(move);
                break;
            case 5:
                field6.setText(move);
                break;
            case 6:
                field7.setText(move);
                break;
            case 7:
                field8.setText(move);
                break;
            case 8:
                field9.setText(move);
                break;
        }
    }

    public void selectField(ActionEvent event) {
        if (GameOver) return;
        Button btn = (Button) event.getSource();
        String btnText = btn.getId();
        int index = Integer.parseInt(btnText.substring(btnText.length() - 1));
        Game.currentGame.play(index - 1);

    }

    public void mouseEntered(MouseEvent me) {

    }

    @FXML
    private void sendMessage(ActionEvent ae) {
        String message = chatTextField.getText().trim();
        if (message.isEmpty()) {
            Helpers.showDialog(Alert.AlertType.ERROR, "Failed", "You can't send empty message", false);
            return;
        }
        Game.currentGame.sendMessage(message);
        chatBox.appendText(Player.player.username + ": " + message + "\n");
        chatTextField.setText("");
    }

    @FXML
    private void share(ActionEvent ae) {
    }

    @FXML
    private void playAgain(ActionEvent ae) {
        App.setRoot("gameRequest");
        if(!Game.currentGame.opponent.username.equalsIgnoreCase("BOT")){
            Game.currentGame = new Game(Game.currentGame.opponent);
            Game.currentGame.sendGameRequest();
        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Record ?");
            alert.setContentText("Do you want to record this game?");
            ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(okButton, noButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) {
                    Server.sendRequest(JSONRequests.playSoloGame("yes").toString());
                } else {
                    Server.sendRequest(JSONRequests.playSoloGame("no").toString());
                }
            });
        }
    }

    @FXML
    private void back(ActionEvent ae) {
        Game.endCurrentGame();
        App.setRoot("PlayerHome");
    }


    public void handleResult(String axis, String result) {
        GameOver = true;
        playAgainbtn.setDisable(false);
        chatTextField.setDisable(true);
        gameGrid.setDisable(true);
        sendMessagebtn.setDisable(true);
        Paint color = Color.BLUE;
        String headerText = "DRAW!";
        if (result.equalsIgnoreCase("win")) {
            color = Color.GREEN;
            new SoundPlayer(SoundPlayer.SOUND.GAME_VICTORY).play();
            headerText = "You Won!";
            highlightAxis(axis,Color.GREEN);
        } else if(result.equalsIgnoreCase("lose")) {
            color = Color.RED;
            new SoundPlayer(SoundPlayer.SOUND.GAME_DEFEAT).play();
            headerText = "You Lost";
            highlightAxis(axis,Color.RED);
        }else{
            new SoundPlayer(SoundPlayer.SOUND.GAME_DRAW).play();
        }
        headerLabel.setTextFill(color);
        headerLabel.setText(headerText);
        headerLabel.setVisible(true);
        backbtn.setDisable(false);
    }
    private void highlightAxis(String axis,Paint color){
    
        switch (axis) {
            case "012":
                field1.setTextFill(color);
                field2.setTextFill(color);
                field3.setTextFill(color);
                break;
            case "345":
                field4.setTextFill(color);
                field5.setTextFill(color);
                field6.setTextFill(color);

                break;
            case "678":
                field7.setTextFill(color);
                field8.setTextFill(color);
                field9.setTextFill(color);

                break;
            case "036":
                field1.setTextFill(color);
                field4.setTextFill(color);
                field7.setTextFill(color);

                break;
            case "147":
                field2.setTextFill(color);
                field5.setTextFill(color);
                field8.setTextFill(color);

                break;
            case "258":
                field3.setTextFill(color);
                field6.setTextFill(color);
                field9.setTextFill(color);

                break;
            case "048":
                field1.setTextFill(color);
                field5.setTextFill(color);
                field9.setTextFill(color);

                break;
            case "246":
                field3.setTextFill(color);
                field5.setTextFill(color);
                field7.setTextFill(color);

                break;
        }
    }
    public void messageRecieved(String sender, String message) {
        System.out.println(message);
        chatBox.appendText(sender + ": " + message + "\n");
    }
}
