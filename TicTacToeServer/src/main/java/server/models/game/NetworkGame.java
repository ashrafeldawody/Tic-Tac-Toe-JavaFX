package server.models.game;

import server.models.Client;
import server.models.DatabaseAccess;
import server.models.JSONRequests;
import server.models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class NetworkGame extends Game{
    public Client opponent;
    public NetworkGame(Client _player, Client _opponent,Boolean record) {
        super(_player);
        opponent = _opponent;
        randomStart();
        notifyGameStart();
        if(record)
            saveGame();
    }
    private void notifyGameStart(){
        gameOwner.sendRequest(JSONRequests.playAccepted(opponent.getUsername(), gameOwner.move, getCurrentTurn()).toString());
        opponent.sendRequest(JSONRequests.playAccepted(gameOwner.getUsername(), opponent.move, getCurrentTurn()).toString());
    }
    public void randomStart() {
        Boolean rand = new Random().nextBoolean();
        if (rand) {
            gameOwner.move = GameMove.X;
            opponent.move = GameMove.O;
        } else {
            gameOwner.move = GameMove.O;
            opponent.move = GameMove.X;
        }
    }
    public void play(Player player, int index){
        if (index > 8) {
            ((Client)player).sendRequest(JSONRequests.play(false, "This Position is not Valid", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else if (!isValidMove(index)) {
            ((Client)player).sendRequest(JSONRequests.play(false, "This Position is not Empty", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else if (!isMyTurn(player.move)) {
            ((Client)player).sendRequest(JSONRequests.play(false, "Not Your Turn!", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else {
            move(player, index);
            gameOwner.sendRequest(JSONRequests.play(true, "", player.getUsername(), index, player.move, getCurrentTurn()).toString());
            opponent.sendRequest(JSONRequests.play(true, "", player.getUsername(), index, player.move, getCurrentTurn()).toString());
            checkWin();
        }
    }


    public boolean hasPlayer(String username) {
        if(gameOwner.getUsername().equalsIgnoreCase(username) || opponent.getUsername().equalsIgnoreCase(username) )
            return true;
        return false;
    }
    public void finishGame(GameMove winner) {
        if(winner == gameOwner.move){
            gameOwner.sendRequest(JSONRequests.gameFinished("win", state).toString());
            opponent.sendRequest(JSONRequests.gameFinished("lose", "").toString());
            gameOwner.incrementPoints();
            saveResult(gameOwner.getUsername());
        }else if(winner == opponent.move){
            gameOwner.sendRequest(JSONRequests.gameFinished("lose", "").toString());
            opponent.sendRequest(JSONRequests.gameFinished("win", state).toString());
            opponent.incrementPoints();
            saveResult(opponent.getUsername());
        }else{
            gameOwner.sendRequest(JSONRequests.gameFinished("draw", "").toString());
            opponent.sendRequest(JSONRequests.gameFinished("draw", "").toString());
            saveResult("DRAW");
        }

        gameOwner.removeGame();
        //store game into db
    }
    public Client getOtherOpponent(String username){
        if(gameOwner.getUsername().equalsIgnoreCase(username))
            return opponent;
        else if(opponent.getUsername().equalsIgnoreCase(username))
            return gameOwner;
        return null;
    }
    public void saveGame(){
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("INSERT INTO games(id,player,playermove,opponent) VALUES((select ifnull(max(rowid), 0) from games) + 1,(select username from players where LOWER(username) = Lower(?)),?,(select username from players where LOWER(username) = Lower(?)));")) {
            st.setString(1, gameOwner.getUsername());
            st.setString(2, String.valueOf(gameOwner.move));
            st.setString(3, opponent.getUsername());
            st.executeUpdate();
            ResultSet resultSet = da.getConnection().createStatement().executeQuery("select max(rowid) from games");
            gameID = resultSet.getInt(1);
            da.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Player sender,String message){
        Client opponent = getOtherOpponent(sender.getUsername());
        if(opponent != null)
            opponent.sendRequest(JSONRequests.sendMessage(message,sender.getUsername()).toString());
    }
}
