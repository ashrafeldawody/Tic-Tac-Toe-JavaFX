/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import server.models.game.*;

/**
 * @author Eagle
 */
public class Client extends Player {
    public static ArrayList<Client> onlineClients = new ArrayList<>();

    private BufferedReader bufferReader;
    private PrintStream printStream;


    public Client(String _username, int _points, BufferedReader _bufferReader, PrintStream _printStream) {
        super(_username, _points);

        notifyAllPlayers();
        setOnline(true);
        bufferReader = _bufferReader;
        printStream = _printStream;
        startListener();
        onlineClients.add(this);
    }

    private void startListener() {
        new Thread(() -> {
            try {
                String str;
                while (!Thread.interrupted() && (str = bufferReader.readLine()) != null) {
                    System.out.println(str);
                    JSONObject jo = new JSONObject(str.trim());
                    String action = (String) jo.get("action");
                    switch (action) {
                        case "get-online-players":
                            sendRequest(JSONRequests.onlinePlayers(getUsername()).toString());
                            break;
                        case "single-game":
                            Boolean record = (jo.getString("record").equalsIgnoreCase("yes"));
                            Game.gamesList.add(new SoloGame(this, SoloGame.Difficulty.EASY,record));
                            break;
                        case "play-request":
                            handlePlayRequest(jo.getString("opponent"));
                            break;
                        case "play-accept":
                            handleAcceptRequest(jo.getString("opponent"),jo.getString("record"));
                            break;
                        case "play-reject":
                            String opponent = jo.getString("opponent");
                            ((NetworkGame) getGame()).getOtherOpponent(opponent).sendRequest(JSONRequests.playRejected(getUsername()).toString());
                            break;
                        case "play":
                            handlePlayMove(jo.getInt("index"));
                            break;
                        case "send-message":
                            if(getGame() != null)
                                ((NetworkGame) getGame()).sendMessage(this,jo.getString("message"));
                            break;
                        case "get-history":
                            sendRequest(JSONRequests.gameHistory(getUsername()).toString());
                            break;
                        case "replay":
                            int gameID = jo.getInt("id");
                            Game.gamesList.add(new ReplayGame(this, gameID));
                            break;
                        case "logout":
                            remove();
                            break;
                        default:
                            sendRequest(Helpers.getStatusObject("Unknown Request"));
                    }
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public Boolean isPlaying() {
        return !(getGame() == null);
    }

    private void notifyAllPlayers() {
        for (Client c : Client.onlineClients) {
            if (!c.getUsername().equalsIgnoreCase(getUsername())) {
                c.sendRequest(JSONRequests.playerConnected(getUsername(), getPoints()).toString());
            }
        }
    }

    //    public Client getMyOpponent(){
//        for (Game game : Game.gamesList) {
//            if (game.hasPlayer(getUsername())) {
//                return ((NetworkGame)game).getOtherOpponent(getUsername());
//            }
//        }
//        return null;
//    }
    public Client getClientByUsername(String username) {
        for (Client client : onlineClients) {
            if (client.getUsername().equalsIgnoreCase(username)) {
                return client;
            }
        }
        return null;
    }


    public void sendRequest(String content) {
        printStream.println(content);
    }


    private void handlePlayRequest(String opponent) {
        Client opponentClient = getClientByUsername(opponent);
        if (opponentClient == null) {
            printStream.println(JSONRequests.playRequest(false, "Player is not online").toString());
            return;
        }
        if (getUsername().equalsIgnoreCase(opponent)) {
            printStream.println(JSONRequests.playRequest(false, "you can't play with youself!").toString());
            return;
        }
        if (opponentClient.isPlaying()) {
            printStream.println(JSONRequests.playRequest(false, "Player is Currently Playing a game").toString());
            return;
        }
        opponentClient.sendRequest(JSONRequests.playRequest(true, getUsername()).toString());
    }

    private void handleAcceptRequest(String opponent,String record) {
        Client opponentClient = getClientByUsername(opponent);
        if (opponentClient == null) {
            printStream.println(JSONRequests.playRequest(false, "Player is not online").toString());
            return;
        }
        Game.gamesList.add(new NetworkGame(this, opponentClient,(record.equalsIgnoreCase("yes"))));
    }


    public void remove() {
        setOnlineOnArrayList(false);
        onlineClients.remove(this);
    }

    private void handlePlayMove(int index) {
        if (getGame() == null) {
            sendRequest(JSONRequests.play(false, "No Active game", getUsername(), 0, GameMove.NONE, GameMove.NONE).toString());
        } else {
            getGame().play(this, index);
        }
    }

    public void removeGame() {
        Iterator<Game> iter = Game.gamesList.iterator();
        while (iter.hasNext()) {
            Game game = iter.next();
            if (game.hasPlayer(getUsername())) {
                iter.remove();
            }
        }

    }

    public Game getGame() {
        for (Game game : Game.gamesList) {
            if (game.hasPlayer(getUsername())) {
                return game;
            }
        }
        return null;
    }
}
