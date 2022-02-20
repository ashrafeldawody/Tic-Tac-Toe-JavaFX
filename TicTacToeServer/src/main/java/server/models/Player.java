/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server.models;

import java.sql.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONArray;
import org.json.JSONObject;
import server.models.game.Game;
import server.models.game.GameMove;

/**
 * @author ashraf
 */
public class Player {
    private StringProperty username;

    public void setUsername(String value) {
        usernameProperty().set(value);
    }

    public String getUsername() {
        return usernameProperty().get();
    }

    public StringProperty usernameProperty() {
        if (username == null) {
            username = new SimpleStringProperty(this, "username");
        }
        return username;
    }

    private IntegerProperty points;

    public void setPoints(int value) {
        pointsProperty().set(value);
    }

    public int getPoints() {
        return pointsProperty().get();
    }

    public IntegerProperty pointsProperty() {
        if (points == null) {
            points = new SimpleIntegerProperty(this, "points");
        }
        return points;
    }

    private BooleanProperty online;

    public void setOnline(Boolean value) {
        onlineProperty().set(value);
    }

    public Boolean getOnline() {
        return onlineProperty().get();
    }

    public BooleanProperty onlineProperty() {
        if (online == null) {
            online = new SimpleBooleanProperty(this, "online");
        }
        return online;
    }

    public GameMove move = GameMove.NONE;
    public static ObservableList<Player> playersList = FXCollections.observableArrayList();

    public Player(String _username) {
        setUsername(_username);
        setPoints(0);
    }

    public Player(String _username, int _points) {
        setUsername(_username);
        setPoints(_points);
    }
    public Player(String _username, int _points,boolean isOnline) {
        setUsername(_username);
        setPoints(_points);
        setOnline(isOnline);
    }

    public Player() {

    }

    public void setOnlineOnArrayList(Boolean status) {
        for (Player p : playersList) {
            if (p.getUsername().equalsIgnoreCase(this.getUsername())) {
                p.setOnline(status);
                break;
            }
        }
    }

    public static Player login(String username,String password) {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("SELECT * FROM players WHERE LOWER(username) = LOWER(?) and password = ?")) {
            st.setString(1, username);
            st.setString(2, password);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    return new Player(username,result.getInt("points"),true);
                }
            }
            da.close();
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    public static Boolean checkUsername(String username) {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("SELECT * FROM players WHERE LOWER(username) = LOWER(?)")) {
            st.setString(1, username);
            try (ResultSet result = st.executeQuery()) {
                if (result.next()) {
                    return true;
                }
            }
            da.close();
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public static void register(String username, String password) {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("INSERT INTO players(username,password) VALUES(?,?)")) {
            st.setString(1, username);
            st.setString(2, password);
            st.executeUpdate();
            Player.getAll();
            da.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incrementPoints() {
        DatabaseAccess da = new DatabaseAccess();
        try (PreparedStatement st = da.getConnection().prepareStatement("UPDATE players set points = points + 10 where lower(username) = lower(?)")) {
            st.setString(1, getUsername());
            st.executeUpdate();
            Player.getAll();
            da.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized ObservableList<Player> getAll() throws Exception {
        playersList.clear();
        String query = "SELECT * FROM players";
        DatabaseAccess da = new DatabaseAccess();
        Statement st = da.getConnection().createStatement();
        ResultSet result = st.executeQuery(query);
        while (result.next()) {
            Player player = new Player(result.getString("username"), result.getInt("points"));
            playersList.add(player);
        }

        da.close();
        return playersList;
    }






}
