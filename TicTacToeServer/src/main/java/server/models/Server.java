package server.models;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private static Thread serverThread;
    private static ServerSocket serverSocket;
    private static int port;
    private static Boolean isRunning = true;
    public static Thread getServer() {
        return serverThread;
    }

    private Server(int _port) {
        port = _port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                if (isRunning) {
                    Socket s = serverSocket.accept();
                    new ClientHandler(s);

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void createServer(int port) {
        isRunning = true;
        serverThread = new Thread(new Server(port));
        serverThread.start();
    }

    public static void stop() {
        isRunning = false;
        if(serverSocket == null) return;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Player.playersList.clear();
        Client.onlineClients.removeAll(Client.onlineClients);
    }

    public static void closeSocket() {
        try {
            if(serverSocket != null && !serverSocket.isClosed()){
                serverSocket.close();
                isRunning = false;
            }  
        } catch (IOException ex) {
            
        }
    }
}

