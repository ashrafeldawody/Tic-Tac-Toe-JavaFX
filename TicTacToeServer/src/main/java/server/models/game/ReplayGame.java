package server.models.game;

import org.json.JSONArray;
import org.json.JSONObject;
import server.models.Client;
import server.models.DatabaseAccess;
import server.models.JSONRequests;
import server.models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReplayGame extends Game {
    Player opponent;
    String winner;
    public ReplayGame(Client player,int gameID) {
        super(player);
        getGame(gameID);

    }
    public static JSONArray getMyGames(String username) {
        JSONArray gamesArray = new JSONArray();
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("select id,player,playermove,opponent,(CASE WHEN playermove == 'X' THEN 'O' ELSE 'X' END) as 'opponentmove',winner ,time FROM games where LOWER(player) = LOWER(?) or LOWER(opponent) = LOWER(?)")) {
            st.setString(1, username);
            st.setString(2, username);
            ResultSet result = st.executeQuery();

            while (result.next()) {
                JSONObject gameJSON = new JSONObject();
                gameJSON.put("gameID", result.getInt("id"));
                gameJSON.put("player", result.getString("player"));
                gameJSON.put("playermove", result.getString("playermove"));
                gameJSON.put("opponent", result.getString("opponent"));
                gameJSON.put("opponentmove", result.getString("opponentmove"));
                gameJSON.put("winner", result.getString("winner"));
                gameJSON.put("datetime", result.getString("time"));
                gamesArray.put(gameJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            da.close();
        }
        return gamesArray;
    }

    private GameMove stringToEnum(String value){
        if(value.equalsIgnoreCase("X"))
            return GameMove.X;
        else
            return GameMove.O;
    }
    public void getGame(int gameID) {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection()
                .prepareStatement("select player,playermove,opponent,(CASE WHEN playermove == 'X' THEN 'O' ELSE 'X' END) as 'opponentmove', " +
                        "(CASE WHEN winner == playermove THEN player ELSE opponent END) as 'winner' FROM games WHERE id = ?")) {
            st.setInt(1, gameID);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    opponent = new Player (result.getString("opponent"),0);
                    opponent.move = stringToEnum(result.getString("opponentmove"));
                    gameOwner.move = stringToEnum(result.getString("playermove"));
                    winner = result.getString("winner");
                    startPlaying(gameID);
                }
            }
            da.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void startPlaying(int gameID) {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("select player,`index`,move from game_moves where game_id = ? order by `order`")) {
            st.setInt(1, gameID);
            try (ResultSet result = st.executeQuery()) {
                while (result.next()) {

                    if(result.getString("player").equalsIgnoreCase(gameOwner.getUsername())){
                        gameOwner.move = stringToEnum(result.getString("move"));
                        play(gameOwner,result.getInt("index"));
                    }else{
                        opponent.move = stringToEnum(result.getString("move"));
                        play(opponent,result.getInt("index"));
                    }
                    Thread.sleep(800);
                }
            }
            gamesList.remove(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            da.close();
            checkWin();
        }
    }

    @Override
    public void randomStart() {

    }

    @Override
    public boolean hasPlayer(String username) {
        return false;
    }

    @Override
    public void finishGame(GameMove winner) {
        if(winner == gameOwner.move){
            gameOwner.sendRequest(JSONRequests.replayFinished(gameOwner.getUsername(), state).toString());
        }else if(winner == opponent.move){
            gameOwner.sendRequest(JSONRequests.replayFinished(opponent.getUsername(), state).toString());
        }else{
            gameOwner.sendRequest(JSONRequests.replayFinished("", "").toString());
        }

        gameOwner.removeGame();
        //store game into db
    }

    @Override
    public void play(Player player, int index) {
        move(player, index);
        gameOwner.sendRequest(JSONRequests.replay(player.getUsername(), index, player.move).toString());
    }

    @Override
    public void saveGame() {

    }

}
