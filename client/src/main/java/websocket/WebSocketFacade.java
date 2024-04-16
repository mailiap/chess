package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import exception.ResponseException;
import ui.GameplayUI;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;


//client

public class WebSocketFacade extends Endpoint {

    public Session session;
    public GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) throws ResponseException {
        try {
            url=url.replace("http", "ws");
            URI socketURI=new URI(url + "/connect");
            this.gameHandler=gameHandler;

            WebSocketContainer container=ContainerProvider.getWebSocketContainer();
            this.session=container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage=new Gson().fromJson(message, ServerMessage.class);
                    try {
                        gameHandler.printMessage(serverMessage, message);
                    } catch (ResponseException e) {
                        throw new RuntimeException(e);
                    } catch (DataAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinPlayerFacade(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        try {
             JoinPlayer player = new JoinPlayer(authToken, gameID, playerColor);
            send(player);
            GameplayUI gamePlay = new GameplayUI(gameID, authToken, playerColor.toString(), this);
            gamePlay.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void joinObserverFacade(String authToken, int gameID) {
        try {
            JoinObserver observer = new JoinObserver(authToken, gameID);
            send(observer);
            GameplayUI gamePlay = new GameplayUI(gameID, authToken, "NULL", this);
            gamePlay.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void makeMoveFacde(String authToken, int gameID, ChessMove move) {
        try {
            MakeMove mover = new MakeMove(authToken, gameID, move);
            send(mover);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void leaveFacade(String authToken, int gameID) {
        try {
            Leave leaver = new Leave(authToken, gameID);
            send(leaver);
//            this.session.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resignFacade(String authToken, int gameID) {
        try {
            Resign resigner = new Resign(authToken, gameID);
            send(resigner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(UserGameCommand command) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }
}