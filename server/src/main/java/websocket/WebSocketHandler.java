package websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.*;
import exception.*;
import model.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.*;

import java.io.*;
import java.sql.*;


//server

@WebSocket
public class WebSocketHandler {

    public WebSocketSessions sessions = new WebSocketSessions();
    public AuthData authData;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException, InvalidMoveException {
        UserGameCommand command=new Gson().fromJson(message, UserGameCommand.class);
        authData=new SQLAuthDAO().getAuthByAuthToken(command.getAuthString());

        if (authData == null) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: unauthorized");
            sendMessage(new Gson().toJson(error), session);
        }

        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(session, message);
            case JOIN_OBSERVER -> joinObserver(session, message);
            case MAKE_MOVE -> makeMove(session, message);
            case LEAVE -> leave(session, message);
            case RESIGN -> resign(session, message);
        }
    }

    public void joinPlayer(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException {
        JoinPlayer player = new Gson().fromJson(message, JoinPlayer.class);
        GameData gameData = new SQLGameDAO().getGameByID(player.getGameID());

        if (player.getPlayerColor().equals(ChessGame.TeamColor.BLACK)) {
            gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
        }

        if (gameData == null) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: game not found");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: game not found");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

//        if (gameData.whiteUsername().equals(new SQLAuthDAO().getUserByAuthToken(authData.authToken())) && gameData.blackUsername().equals(new SQLAuthDAO().getUserByAuthToken(authData.authToken()))) {
//            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: unauthorized");
//            sendMessage(new Gson().toJson(error), session);
//            return;
//        }

        // add user
        sessions.addSessionToGame(player.getGameID(), player.getAuthString(), session);

        // load game and send to client
        LoadGame gameNotify = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), player.getPlayerColor());
        sendMessage(new Gson().toJson(gameNotify), session);

        // send notification to all clients
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,authData.username() + " has joined the " + player.getPlayerColor() + " team");
        broadcastMessage(player.getGameID(), new Gson().toJson(notification), session);
    }


    public void joinObserver(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException {
        JoinObserver observer = new Gson().fromJson(message, JoinObserver.class);
        GameData gameData = new SQLGameDAO().getGameByID(observer.getGameID());

        if (gameData == null) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "joinObserver Error: game = null");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

//        if (gameData.whiteUsername() == null && gameData.blackUsername() == null) {
//            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "joinObserver Error: game username = null");
//            sendMessage(new Gson().toJson(error), session);
//            return;
//        }

        // add user
        sessions.addSessionToGame(observer.getGameID(), observer.getAuthString(), session);

        // load game and send to client
        LoadGame gameNotify = new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game(), null);
        sendMessage(new Gson().toJson(gameNotify), session);

        // send notification to all clients
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, authData.username() + " is observing the game");
        broadcastMessage(observer.getGameID(), new Gson().toJson(notification), session);
    }

    public void makeMove(Session session, String message) throws ResponseException, DataAccessException, SQLException, IOException, InvalidMoveException {
        MakeMove mover=new Gson().fromJson(message, MakeMove.class);
        GameData gameData = new SQLGameDAO().getGameByID(mover.getGameID());
        boolean whitePlayer=false;
        boolean blackPlayer=false;
        boolean observer=false;

        // verify the validity of the move
        if (mover.getAuthString().equals(authData.authToken()) && gameData.whiteUsername().equals(authData.username())) {
            whitePlayer=true;
        } else if (mover.getAuthString().equals(authData.authToken()) && gameData.blackUsername().equals(authData.username())) {
            blackPlayer=true;
        } else if (!whitePlayer && !blackPlayer) {
            observer=true;
        }

        if (observer) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: observer cannot make moves");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        ChessGame.TeamColor teamColor = whitePlayer ? ChessGame.TeamColor.WHITE: ChessGame.TeamColor.BLACK;
        if (gameData.game().getTeamTurn() != teamColor) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: not your turn");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        // make move
        gameData.game().isInCheckmate(teamColor);
        gameData.game().isInStalemate(teamColor);
        gameData.game().makeMove(mover.getMove());

        // update game in database
        new SQLGameDAO().updateGame(gameData.game(), gameData.gameID());

        // send updated game to all clients
        LoadGame gameNotify=new LoadGame(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
        sendMessage(new Gson().toJson(gameNotify), session);
        broadcastMessage(mover.getGameID(), new Gson().toJson(gameNotify), session);

        // send notification to all clients
        Notification notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, authData.username() + " made the move " + mover.getMove());
        broadcastMessage(mover.getGameID(), new Gson().toJson(notification), session);
    }

    public void leave(Session session, String message) throws IOException, ResponseException, DataAccessException, SQLException {
        Leave leaver = new Gson().fromJson(message, Leave.class);

        // remove user
        sessions.removeSessionFromGame(leaver.getGameID(), leaver.getAuthString(), session);

        // send notification to all clients
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION,authData.username() + " left the game");
        broadcastMessage(leaver.getGameID(), new Gson().toJson(notification), session);
    }

    public void resign(Session session, String message) throws IOException, ResponseException, DataAccessException, SQLException {
        Resign resigner = new Gson().fromJson(message, Resign.class);
        GameData gameData = new SQLGameDAO().getGameByID(resigner.getGameID());


        if (!gameData.whiteUsername().equals(new SQLAuthDAO().getUserByAuthToken(resigner.getAuthString())) && !gameData.blackUsername().equals(new SQLAuthDAO().getUserByAuthToken(resigner.getAuthString()))) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: Observer cannot resign");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        if (gameData.game().getIsResigned()) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: Player has already resigned");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        if (gameData == null) {
            Error error=new Error(ServerMessage.ServerMessageType.ERROR, "ERROR: invalid game id");
            sendMessage(new Gson().toJson(error), session);
            return;
        }

        // mark game as over
        //update game in database
        gameData.game().setIsResgined(true);

        // remove userd
        sessions.removeSessionFromGame(resigner.getGameID(), resigner.getAuthString(), session);

        // send notification
        Notification notification = new Notification(ServerMessage.ServerMessageType.NOTIFICATION, authData.username() + " resigned from the game");
        broadcastMessage(resigner.getGameID(), new Gson().toJson(notification), session);
    }

    public void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    public void broadcastMessage(int gameID, String message, Session session) throws IOException {
        // get sessions
        var gameSessions=sessions.getSessionsForGame(gameID);

        // loop through and send message except for the session passed through
        for (var otherSessions : gameSessions.values()) {
            if (!otherSessions.equals(session)) {
                sendMessage(message, otherSessions);
            }
        }
    }
}