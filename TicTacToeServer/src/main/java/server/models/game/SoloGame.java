package server.models.game;

import server.models.Client;
import server.models.DatabaseAccess;
import server.models.JSONRequests;
import server.models.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class SoloGame extends Game {
    public enum Difficulty {EASY, HARD}

    private Player virtualPlayer = new Player("Bot");
    private Difficulty difficulty = Difficulty.EASY;

    public SoloGame(Client player, Difficulty _difficulty,Boolean record) {
        super(player);
        difficulty = _difficulty;
        randomStart();
        gameOwner.sendRequest(JSONRequests.singleGameStarted(gameOwner.move,turn).toString());
        gamesList.add(this);
        if(record)
            saveGame();
    }

    public void randomStart() {
        boolean rand = new Random().nextBoolean();
        if (rand) {
            gameOwner.move = GameMove.X;
            virtualPlayer.move = GameMove.O;
        } else {
            gameOwner.move = GameMove.O;
            virtualPlayer.move = GameMove.X;
        }
        turn = gameOwner.move;
    }

    @Override
    public boolean hasPlayer(String username) {
        if(gameOwner.getUsername().equalsIgnoreCase(username))
            return true;
        return false;
    }

    public void virtualPlay() {
        if(gameOver) return;
        if (difficulty == Difficulty.EASY) {
            Random r = new Random();
            int randIndex = r.nextInt(9);
            while (true) {
                if (isValidMove(randIndex)) {
                    move(virtualPlayer,randIndex);
                    gameOwner.sendRequest(JSONRequests.play(true, "", virtualPlayer.getUsername(), randIndex, virtualPlayer.move, getCurrentTurn()).toString());
                    break;
                }
                randIndex = r.nextInt(9);
            }
        } else {

        }
    }
    public void finishGame(GameMove winner) {
        if(winner == gameOwner.move){
            gameOwner.sendRequest(JSONRequests.gameFinished("win", state).toString());
            gameOwner.incrementPoints();
            saveResult(gameOwner.getUsername());
        }else if(winner == virtualPlayer.move){
            gameOwner.sendRequest(JSONRequests.gameFinished("lose", state).toString());
            saveResult("Computer");
        }else{
            gameOwner.sendRequest(JSONRequests.gameFinished("draw", "").toString());
            saveResult("DRAW");
        }

        gameOwner.removeGame();
        //store game into db
    }

    @Override
    public void play(Player player, int index) {
        if (index > 8) {
            ((Client)player).sendRequest(JSONRequests.play(false, "This Position is not Valid", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else if (!isValidMove(index)) {
            ((Client)player).sendRequest(JSONRequests.play(false, "This Position is not Empty", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else if (!isMyTurn(player.move)) {
            ((Client)player).sendRequest(JSONRequests.play(false, "Not Your Turn!", player.getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else {
            move(player, index);
            gameOwner.sendRequest(JSONRequests.play(true, "", player.getUsername(), index, player.move, getCurrentTurn()).toString());
            checkWin();
            virtualPlay();
            checkWin();
            turn = gameOwner.move;
        }
    }
    public void saveGame(){
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("INSERT INTO games(id,player,playermove,opponent) VALUES((select ifnull(max(rowid), 0) from games) + 1,(select username from players where LOWER(username) = Lower(?)),?,null);")) {
            st.setString(1, gameOwner.getUsername());
            st.setString(2, String.valueOf(gameOwner.move));
            st.executeUpdate();
            ResultSet resultSet = da.getConnection().createStatement().executeQuery("select max(rowid) from games");
            gameID = resultSet.getInt(1);
            da.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
