package websocket;

import com.google.gson.Gson;
import dataAccess.*;
import exception.*;
import model.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.*;
import java.sql.*;
import java.util.*;


//server

@WebSocket
public class WebSocketHandler {

    public WebSocketSessions sessions = new WebSocketSessions();
    private String username = "";

//    @OnWebSocketConnect
//    public void onConnect(Session session) {}
//
//    @OnWebSocketClose
//    public void OnClose(Session session) {}
//
//    @OnWebSocketError
//    public void OnError(Throwable throwable) {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        GameData gameData = new Gson().fromJson(message, GameData.class);

        // check auth token
        username = new SQLAuthDAO().getUserByAuthToken(command.getAuthString());
        Collection<GameData> list=new SQLGameDAO().getGames();
        if (username == null || !list.contains(gameData.gameID()) || list == null) {
            Error error = new Error(ServerMessage.ServerMessageType.ERROR, "Error: unauthorized");
            sendMessage(new Gson().toJson(error), session);
        } else {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> joinPlayer(session, message);
                case JOIN_OBSERVER -> joinObserver(session, message);
                case MAKE_MOVE -> makeMove(session);
                case LEAVE -> leave(session, message);
                case RESIGN -> resign(session);
            }
        }
    }
    public void joinPlayer(Session session, String message) throws IOException, ResponseException, DataAccessException, SQLException {
        JoinPlayer player = new Gson().fromJson(message, JoinPlayer.class);

        sessions.addSessionToGame(player.getGameID(), player.getAuthString(), session);
        // get game form database using gameDAO
        GameData game = new SQLGameDAO().getGameByID(player.getGameID());
        // set message as Json new Notoificatio ("")

        LoadGame gameNotify = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        sendMessage(new Gson().toJson(gameNotify), session); //toJson
        // make new Notification
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the " + player.getPlayerColor() + " game");
        broadcastMessage(player.getGameID(), notification.getMessage(), session);
    }


    public void joinObserver(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException {
        JoinObserver observer = new Gson().fromJson(message, JoinObserver.class);

        sessions.addSessionToGame(observer.getGameID(), observer.getAuthString(), session);
        // get game form database using gameDAO
        GameData game = new SQLGameDAO().getGameByID(observer.getGameID());
        // set message as Json new Notoificatio ("")

        LoadGame gameNotify = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        sendMessage(new Gson().toJson(gameNotify), session); //toJson
        // make new Notification
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has joined the game");
        broadcastMessage(observer.getGameID(), notification.getMessage(), session);
    }

    public void makeMove(Session session) throws ResponseException, DataAccessException {
    }

    public void leave(Session session, String message) throws IOException, ResponseException, DataAccessException, SQLException {
        // remove user
        // update game in database
        // send notification
        Leave leaver = new Gson().fromJson(message, Leave.class);
        sessions.removeSessionFromGame(leaver.getGameID(), leaver.getAuthString(), session);
        // make new Notification
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left the game");
        broadcastMessage(leaver.getGameID(), notification.getMessage(), session);
    }

    public void resign(Session session) throws ResponseException, DataAccessException {
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastMessage(int gameID, String message, Session session) throws IOException {
        sessions.getSessionsForGame(gameID);
        // getSeesionsForGAme
        // loop through
        // sendMessage except for this session passed throguh
        var removeList = new ArrayList<Session>();
        var gameSessions = sessions.getSessionsForGame(gameID);
        for (var c : gameSessions.values()) {
            if (c.isOpen()) {
                if (!c.equals(session)) {
                    sendMessage(message, c);
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            sessions.removeSession(session);
        }
    }

}