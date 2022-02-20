/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.models.game;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;
import server.models.Client;
import server.models.DatabaseAccess;
import server.models.Player;

/**
 * @author Eagle
 */
public abstract class Game {
    protected int gameID = 0;
    protected TreeMap<Integer, Move> movesList = new TreeMap<>();
    protected Boolean gameOver = false;
    protected final GameMove[][] board = {
            {GameMove.NONE, GameMove.NONE, GameMove.NONE},
            {GameMove.NONE, GameMove.NONE, GameMove.NONE},
            {GameMove.NONE, GameMove.NONE, GameMove.NONE},
    };

    protected int winStates[][] = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6},
    };

    public GameMove getIndexValue(int index) {
        switch (index) {
            case 0:
                return board[0][0];
            case 1:
                return board[0][1];
            case 2:
                return board[0][2];
            case 3:
                return board[1][0];
            case 4:
                return board[1][1];
            case 5:
                return board[1][2];
            case 6:
                return board[2][0];
            case 7:
                return board[2][1];
            default:
                return board[2][2];
        }
    }

    public void setIndexValue(int index, GameMove value) {
        switch (index) {
            case 0:
                board[0][0] = value;
                break;
            case 1:
                board[0][1] = value;
                break;
            case 2:
                board[0][2] = value;
                break;
            case 3:
                board[1][0] = value;
                break;
            case 4:
                board[1][1] = value;
                break;
            case 5:
                board[1][2] = value;
                break;
            case 6:
                board[2][0] = value;
                break;
            case 7:
                board[2][1] = value;
                break;
            default:
                board[2][2] = value;
                break;
        }
    }

    protected GameMove turn = GameMove.NONE;
    public String state;
    public static ArrayList<Game> gamesList = new ArrayList<>();
    public Client gameOwner;
    public Game(Client player) {
        gameOwner = player;
        randomTurn();
    }

    public abstract void randomStart();

    public abstract boolean hasPlayer(String username);

    public abstract void finishGame(GameMove winner);

    public abstract void saveGame();
    public abstract void play(Player player, int index);

    private void randomTurn() {
        turn = new Random().nextBoolean() ? GameMove.O : GameMove.X;
    }

    public GameMove getCurrentTurn() {
        return turn;
    }


    public boolean isMyTurn(GameMove move) {
        if (turn == move) {
            return true;
        }
        return false;
    }

    public void move(Player player, int index) {
        if(gameOver) return;
        if (player.move == GameMove.X)
            turn = GameMove.O;
        else
            turn = GameMove.X;
        new Move(index, player);
    }

    public void checkWin() {
        if(gameOver) return;
        for (int[] state : winStates) {
            if (getIndexValue(state[0]) == getIndexValue(state[1]) && getIndexValue(state[1]) == getIndexValue(state[2]) && getIndexValue(state[1]) != GameMove.NONE) {
                this.state = state[0] + "" + state[1] + "" + state[2];
                finishGame(getIndexValue(state[0]));
                gameOver = true;
                return;
            }
        }
        if (movesList.size() == 9) {
            finishGame(GameMove.NONE);
            gameOver = true;
        }
    }
    public void saveResult(String winner){
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("UPDATE games set winner = ?, `time` = DATE('now') where id = ?")) {
            st.setString(1, winner);
            st.setInt(2, gameID);
            st.executeUpdate();
            da.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Boolean isValidMove(int index) {
        return (getIndexValue(index) == GameMove.NONE);
    }

    protected class Move {
        int index;
        Player player;

        public Move(int _index, Player _player) {
            index = _index;
            player = _player;
            movesList.put(movesList.size(), this);
            execute();
            SaveMove();
        }
        public void SaveMove(){
            if(gameID == 0) return;
            Connection con = new DatabaseAccess().getConnection();
            try (PreparedStatement st = con.prepareStatement("INSERT INTO game_moves(game_id,move,`index`,`order`,player) VALUES (?,?,?,?,?)")) {
                st.setInt(1, gameID);
                st.setString(2, String.valueOf(player.move));
                st.setInt(3, index);
                st.setInt(4, movesList.size());
                st.setString(5, player.getUsername());
                st.executeUpdate();
                Player.getAll();
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        private void execute() {
            setIndexValue(index, player.move);
        }
    }

}
