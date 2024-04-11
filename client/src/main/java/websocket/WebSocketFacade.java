package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.GameplayUI;
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
                public void onMessage(String s) {
                    ServerMessage serverMessage=new Gson().fromJson(s, ServerMessage.class);
                    gameHandler.printMessage(serverMessage);
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
            String playerJoined = new Gson().toJson(player);
            send(playerJoined);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void joinObserverFacade(String authToken, int gameID) {
        try {
            JoinObserver observer = new JoinObserver(authToken, gameID);
            String observerJoined = new Gson().toJson(observer);
            send(observerJoined);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void leaveFacade(String authToken, int gameID) {
        try {
            Leave leaver = new Leave(authToken, gameID);
            String userLeft = new Gson().toJson(leaver);
            send(userLeft);
//            this.session.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(new Gson().toJson(msg));
    }
}