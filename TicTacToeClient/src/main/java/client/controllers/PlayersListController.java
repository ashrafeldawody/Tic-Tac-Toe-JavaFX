package client.controllers;

import client.App;
import client.models.Game;
import client.models.Helpers;
import client.models.Player;
import client.models.ResponseHandler;
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

public class PlayersListController implements Initializable {
    @FXML
    private TableView table;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn usernameCol = new TableColumn("Name");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn pointsCol = new TableColumn("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));
        pointsCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getColumns().addAll(usernameCol, pointsCol);
        table.getItems().setAll(ResponseHandler.playersList);
    }
    @FXML
    private void back(ActionEvent ae) throws IOException {
        App.setRoot("PlayerHome");
    }

    @FXML
    private void invite(ActionEvent ae){
        if(table.getSelectionModel().isEmpty()){
            Helpers.showDialog(Alert.AlertType.ERROR,  "Failed", "No Player Seleted", false);
            return;
        }
        App.setRoot("gameRequest");
        Player p = (Player) table.getSelectionModel().getSelectedItem();
        ResponseHandler.tempOpponentUsername = p.username;
        Game.currentGame = new Game(p);
        Game.currentGame.sendGameRequest();
    }
    @FXML
    private void refresh(ActionEvent ae){
        Player.getOnlineList();
        table.getItems().setAll(ResponseHandler.playersList);
        table.refresh();
    }
    @FXML
    private void mouseEntered(MouseEvent ae){
        new SoundPlayer(SoundPlayer.SOUND.TICK).play();
    }

}
