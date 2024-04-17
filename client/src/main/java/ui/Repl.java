package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import exception.ResponseException;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.GameHandler;

import static ui.EscapeSequences.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Scanner;

public class Repl implements GameHandler {
    private final ChessClient client;

    public Repl(String serverUrl) throws ResponseException {
        client = new ChessClient(serverUrl, this);
    }

    public void run() throws ResponseException, IOException, URISyntaxException {
        System.out.println(SET_TEXT_COLOR_MAGENTA + " ♕ " + "Welcome to the chess game. Sign in to start.");
        System.out.println(RESET_TEXT_COLOR);
        System.out.print(client.help());

        Scanner scanner=new Scanner(System.in);
        var result="";
        while (!result.contains("Exiting")) {
            System.out.print("\n" + RESET_TEXT_COLOR + ">>> ");
            String line=scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg=e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void printMessage(ServerMessage serverType, String message) throws ResponseException, DataAccessException {
//        System.out.println(SET_TEXT_COLOR_YELLOW + "I don't know what this is doing" + RESET_TEXT_COLOR);
        switch (serverType.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGame loadGame =new Gson().fromJson(message, LoadGame.class);

                if (loadGame.getPlayerColor() == null) {
                    loadGame.getGame().setTeamTurn(null);
                } else if (loadGame.getPlayerColor().equals(ChessGame.TeamColor.BLACK)) {
                    loadGame.getGame().setTeamTurn(ChessGame.TeamColor.BLACK);
                } else {
                    loadGame.getGame().setTeamTurn(ChessGame.TeamColor.WHITE);
                }
                
                System.out.println();
                DrawGameBoard.drawChessboard(loadGame.getGame());
                System.out.print("\n" + RESET_TEXT_COLOR + "[GAMEPLAY] >>> ");
            }
            case NOTIFICATION -> {
                Notification notification =new Gson().fromJson(message, Notification.class);
//                Notification notification=new Notification(ServerMessage.ServerMessageType.NOTIFICATION, message);
                System.out.println(notification.getMessage());
            }
            case ERROR -> {
                Error error =new Gson().fromJson(message, Error.class);
//                Error errorSent=new Error(ServerMessage.ServerMessageType.ERROR, message);
                System.out.println(error.getErrorMessage());
            }
        }
    }
}