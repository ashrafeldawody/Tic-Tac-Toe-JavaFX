package client.controllers;

import client.App;
import client.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GamesListController implements Initializable {
    @FXML
    private TableView table;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn idCol = new TableColumn("Game ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn playerCol = new TableColumn("Player");
        playerCol.setCellValueFactory(new PropertyValueFactory<>("player"));

        TableColumn playermoveCol = new TableColumn("Move");
        playermoveCol.setCellValueFactory(new PropertyValueFactory<>("playermove"));

        TableColumn opponentCol = new TableColumn("Opponent");
        opponentCol.setCellValueFactory(new PropertyValueFactory<>("opponent"));

        TableColumn opponentmoveCol = new TableColumn("Move");
        opponentmoveCol.setCellValueFactory(new PropertyValueFactory<>("opponentmove"));

        TableColumn datetimeCol = new TableColumn("datetime");
        datetimeCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        TableColumn winnerCol = new TableColumn("Winner");
        winnerCol.setCellValueFactory(new PropertyValueFactory<>("winner"));


        table.getColumns().addAll(idCol,playerCol,playermoveCol,opponentCol,opponentmoveCol,winnerCol,datetimeCol);
        table.getItems().setAll(RecordedGame.recordedGames);
    }
    @FXML
    private void back(ActionEvent ae) throws IOException {
        App.setRoot("PlayerHome");
    }
    @FXML
    private void replay(ActionEvent ae){
        if(table.getSelectionModel().isEmpty()){
            Helpers.showDialog(Alert.AlertType.ERROR,  "Failed", "No Game Selected", false);
            return;
        }
        RecordedGame.current = (RecordedGame) table.getSelectionModel().getSelectedItem();
        App.setRoot("ReplayWindow");
        Server.sendRequest(JSONRequests.replay().toString());
    }
    @FXML
    private void mouseEntered(MouseEvent ae){
        new SoundPlayer(SoundPlayer.SOUND.TICK).play();
    }

}
