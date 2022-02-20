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
public class GameReplayWindowController implements Initializable {
    public static GameReplayWindowController me;


    @FXML
    private GridPane gameGrid;
    @FXML
    private Label headerLabel;
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


    public void initialize(URL url, ResourceBundle rb) {
        me = this;
        myUsername.setText(RecordedGame.current.player);
        myMove.setText(RecordedGame.current.playermove);
        opponentUsername.setText(RecordedGame.current.opponent);
        opponentMove.setText(RecordedGame.current.opponentmove);

    }

    public void setMove(int index, String move) {
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


    public void mouseEntered(MouseEvent me) {

    }



    @FXML
    private void back(ActionEvent ae) {
        Game.endCurrentGame();
        App.setRoot("PlayerHome");
    }


    public void handleResult(String axis, String winner) {
        if(winner.equalsIgnoreCase(Player.player.getUsername())){
            new SoundPlayer(SoundPlayer.SOUND.GAME_VICTORY).play();
            highlightAxis(axis,Color.GREEN);
                headerLabel.setText("You Won");
        }else if(!winner.isBlank()){
            new SoundPlayer(SoundPlayer.SOUND.GAME_DEFEAT).play();
            highlightAxis(axis,Color.RED);
            headerLabel.setTextFill(Color.RED);
            headerLabel.setText(winner + " Won");
        }else{
            new SoundPlayer(SoundPlayer.SOUND.GAME_DRAW).play();
            headerLabel.setText("DRAW");
        }

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
}
