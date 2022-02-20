/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * @author ashra
 */
public class Player {
    public String username;
    public int points = 0;
    public Boolean online;
    public String move;
    public static Player player = null;

    public String getUsername() {
        return username;
    }
    public int getPoints(){
        return points;
    }

    public Boolean getOnline() {
        return online;
    }

    public Player(String _username, int _points){
        username =_username;
        points = _points;
    }
    public Player(String _username, String _move){
        username =_username;
        move = _move;
    }
    public static void getOnlineList(){
        Server.sendRequest(JSONRequests.onlinePlayers().toString());
    }

    public static void login(String username,String password){
        Server.sendRequest(JSONRequests.login(username,password).toString());
    }
    public static void logout(){
        Server.sendRequest(JSONRequests.logout().toString());
    }

    public static void register(String username,String password){
        Server.sendRequest(JSONRequests.register(username,password).toString());
    }

}
