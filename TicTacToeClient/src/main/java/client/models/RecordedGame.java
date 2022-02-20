package client.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class RecordedGame{
    public static RecordedGame current;
    public static ArrayList<RecordedGame> recordedGames = new ArrayList<RecordedGame>();

    public int id;
    public String player;
    public String playermove;
    public String opponent;
    public String opponentmove;
    public String winner;
    public String datetime;

    public int getId() {
        return id;
    }
    public String getPlayer(){
        return player;
    }
    public String getPlayermove(){
        return playermove;
    }
    public String getOpponent(){
        return opponent;
    }
    public String getOpponentmove(){
        return opponentmove;
    }
    public String getWinner(){
        return winner;
    }
    public String getDatetime(){
        return datetime;
    }


    public RecordedGame(int id, String player, String playermove, String opponent, String opponentmove, String winner, String datetime){
        this.id = id;
        this.player = player;
        this.playermove = playermove;
        this.opponent = opponent;
        this.opponentmove = opponentmove;
        this.winner = winner;
        this.datetime = datetime;
    }
    public static void parseMyHistory(JSONObject response){
        recordedGames.clear();
        for (Object object : response.getJSONArray("games")) {
            JSONObject parsedObj = ((JSONObject) object);
            int id = parsedObj.getInt("gameID");
            String datetime = !parsedObj.isNull("datetime") ? parsedObj.getString("datetime") : "";
            String player = parsedObj.getString("player");
            String playermove = parsedObj.getString("playermove");
            String opponent = !parsedObj.isNull("opponent") ? parsedObj.getString("opponent") : "Computer";
            String opponentmove = parsedObj.getString("opponentmove");
            String winner = !parsedObj.isNull("winner") ? parsedObj.getString("winner") : "";

            recordedGames.add(new RecordedGame(id,player,playermove, opponent,opponentmove,winner,datetime));
        }
    }
}