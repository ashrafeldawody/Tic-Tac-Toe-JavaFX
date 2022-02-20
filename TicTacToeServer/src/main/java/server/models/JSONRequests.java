package server.models;

import org.json.JSONArray;
import org.json.JSONObject;
import server.models.game.GameMove;
import server.models.game.ReplayGame;

public class JSONRequests {
    public static JSONObject login(Boolean success, String msg, Player player) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "login");
        jsonObject.put("status", (success ? "success" : "fail"));
        if (player != null) {
            JSONObject playerJson = new JSONObject();
            playerJson.put("username", player.getUsername());
            playerJson.put("points", player.getPoints());
            jsonObject.put("player", playerJson);
        }
        jsonObject.put("message", msg);
        return jsonObject;
    }

    public static JSONObject register(Boolean success, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "register");
        jsonObject.put("status", (success ? "success" : "fail"));
        jsonObject.put("message", msg);
        return jsonObject;
    }

    public static JSONObject onlinePlayers(String myUsername) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonResponse = new JSONArray();
        for (Client client : Client.onlineClients) {
            if (client.getUsername().equalsIgnoreCase(myUsername)) continue;
            JSONObject playerJSON = new JSONObject();
            playerJSON.put("username", client.getUsername());
            playerJSON.put("points", client.getPoints());
            jsonResponse.put(playerJSON);
        }
        jsonObject.put("type", "get-online-players");
        jsonObject.put("players", jsonResponse);
        return jsonObject;
    }

    public static JSONObject playRequest(Boolean success, String opponent) {
        JSONObject request = new JSONObject();
        request.put("type", "play-request");
        request.put("status", (success ? "success" : "fail"));
        request.put("opponent", opponent);
        return request;
    }

    public static JSONObject playAccepted(String opponent, GameMove move,GameMove turn) {
        JSONObject request = new JSONObject();
        request.put("type", "game-start");
        request.put("status", "success");
        request.put("move", String.valueOf(move));
        request.put("turn", String.valueOf(turn));
        request.put("opponent", opponent);
        return request;
    }
    public static JSONObject singleGameStarted(GameMove move,GameMove turn) {
        JSONObject request = new JSONObject();
        request.put("type", "play-single-start");
        request.put("status", "success");
        request.put("move", String.valueOf(move));
        request.put("turn", String.valueOf(turn));
        request.put("opponent", "BOT");

        return request;
    }

    public static JSONObject playRejected(String opponent) {
        JSONObject request = new JSONObject();
        request.put("type", "game-reject");
        request.put("opponent", opponent);
        return request;
    }

    public static JSONObject play(Boolean success, String msg, String playerUsername, int index, GameMove move, GameMove turn) {
        JSONObject request = new JSONObject();
        request.put("type", "play");
        request.put("player", playerUsername);
        request.put("status", (success ? "success" : "fail"));
        if (success) {
            request.put("move", String.valueOf(move));
            request.put("index", index);
            request.put("turn", String.valueOf(turn));
        } else {
            request.put("message", msg);
        }
        return request;
    }

    public static JSONObject replay(String playerUsername, int index, GameMove move) {
        JSONObject request = new JSONObject();
        request.put("type", "replay");
        request.put("player", playerUsername);
        request.put("move", String.valueOf(move));
        request.put("index", index);
        return request;
    }

    public static JSONObject sendMessage(String message, String from) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "message");
        jsonObject.put("message", message);
        jsonObject.put("from", from);
        return jsonObject;
    }

    public static JSONObject gameFinished(String status, String axis) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "game-finish");
        jsonObject.put("status", status);
        jsonObject.put("axis", axis);
        return jsonObject;
    }

    public static JSONObject replayFinished(String winner,String axis) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "replay-finish");
        jsonObject.put("winner", winner);
        jsonObject.put("axis", axis);
        return jsonObject;
    }

    public static JSONObject gameHistory(String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", "game-history");
        jsonObject.put("games", ReplayGame.getMyGames(username));
        return jsonObject;
    }

    public static JSONObject playerConnected(String username, int points) {
        JSONObject request = new JSONObject();
        request.put("type", "player-connected");
        request.put("player", username);
        request.put("points", points);
        return request;
    }

}
