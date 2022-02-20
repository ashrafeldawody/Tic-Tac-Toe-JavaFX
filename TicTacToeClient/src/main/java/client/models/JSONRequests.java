package client.models;

import org.json.JSONObject;
public class JSONRequests {
    public static JSONObject login(String username,String password){
        JSONObject request = new JSONObject();
        request.put("action", "login");
        request.put("username", username);
        request.put("password", password);
        return request;
    }
    public static JSONObject register(String username,String password){
        JSONObject request = new JSONObject();
        request.put("action", "register");
        request.put("username", username);
        request.put("password", password);
        return request;
    }
    public static JSONObject onlinePlayers(){
        JSONObject request = new JSONObject();
        request.put("action", "get-online-players");
        return request;
    }
    public static JSONObject playRequest(String opponent){
        JSONObject request = new JSONObject();
        request.put("action", "play-request");
        request.put("opponent", opponent);
        return request;
    }
    public static JSONObject playAccept(String record){
        JSONObject request = new JSONObject();
        request.put("action", "play-accept");
        request.put("record", record);
        request.put("opponent", ResponseHandler.tempOpponentUsername);
        return request;
    }
    public static JSONObject playSoloGame(String record){
        JSONObject request = new JSONObject();
        request.put("action", "single-game");
        request.put("record", record);
        return request;
    }
    public static JSONObject playReject(){
        JSONObject request = new JSONObject();
        request.put("action", "play-reject");
        request.put("opponent", ResponseHandler.tempOpponentUsername);
        return request;
    }
    public static JSONObject play(int index){
        JSONObject request = new JSONObject();
        request.put("action", "play");
        request.put("index", index);
        return request;
    }
    public static JSONObject gameHistory(){
        JSONObject request = new JSONObject();
        request.put("action", "get-history");
        return request;
    }
    public static JSONObject messageSend(String msg){
        JSONObject request = new JSONObject();
        request.put("action", "send-message");
        request.put("message", msg);
        return request;
    }
    public static JSONObject replay(){
        JSONObject request = new JSONObject();
        request.put("action", "replay");
        request.put("id", RecordedGame.current.id);
        return request;
    }
    public static JSONObject logout(){
        JSONObject request = new JSONObject();
        request.put("action", "logout");
        return request;
    }

}
