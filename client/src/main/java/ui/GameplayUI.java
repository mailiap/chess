package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import exception.ResponseException;
import model.GameData;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.Leave;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayUI {
    private String playerColor="";
    private int inputGameID=0;
    private String command="";
    private String authToken="";
    private final Scanner scanner;
    private DrawGameBoard gameBoard;
    private WebSocketFacade wsFacade;

    public GameplayUI(int inputGameID, String authToken, String playerColor, WebSocketFacade wsFacade) {
        this.scanner=new Scanner(System.in);
        this.gameBoard=new DrawGameBoard();
        this.inputGameID=inputGameID;
        this.authToken=authToken;
        this.playerColor=playerColor;
        this.wsFacade=wsFacade;
    }

    public GameplayUI() {
        this.scanner=new Scanner(System.in);
        this.gameBoard=new DrawGameBoard();
        this.inputGameID=inputGameID;
        this.playerColor=playerColor;
        this.wsFacade=wsFacade;
    }

    public void run() throws ResponseException, SQLException, DataAccessException {
        while (!command.contains("leave")) {
            System.out.print("\n" + RESET_TEXT_COLOR + "[GAMEPLAY] >>> ");
            command=scanner.nextLine();
            var tokens=command.toLowerCase().split(" ");
            var choice=(tokens.length > 0) ? tokens[0] : "help";
            var params=Arrays.copyOfRange(tokens, 0, tokens.length);

            switch (choice) {
                case "help" -> System.out.print(help());
                case "redraw" -> redraw();
                case "leave" -> leave(params);
                case "move" -> move(params);
                case "resign" -> resign(params);
                case "highlight" -> System.out.print(highlight());
                default -> System.out.print("Unexpected value: " + command + "\n");
            }
        }
    }

    public String help() {
        return """
                help
                redraw
                leave <ID>
                move <ID> <MOVE>
                resign <ID>
                highlight <PIECE>
                """;
    }

    public void redraw() throws ResponseException, DataAccessException, SQLException {
        var out=new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        // save game after getting the load game notification
        // pass in game
        GameData gameData=new SQLGameDAO().getGameByID(inputGameID);

        if (playerColor.equals("BLACK")) {
            gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
        }

        gameBoard.drawChessboard(out, gameData.game());

        if (playerColor.equals("WHITE")) {
            System.out.println(SET_TEXT_COLOR_BLUE);
            System.out.println(String.format("You are on the " + playerColor + " team"));

        } else if (playerColor.equals("BLACK")) {
            System.out.println(SET_TEXT_COLOR_RED);
            System.out.println(String.format("You are on the " + playerColor + " team"));

        } else if (playerColor.equals("NULL")) {
            System.out.println(SET_TEXT_COLOR_YELLOW);
            System.out.println(String.format("You are an observer."));
        }
    }

    public void move(String... params) {
//        if (params.length != 1) {
//            int gameID=Integer.parseInt(params[1]);
//            var postitions=params[2].split(" ");
//
//
//
//            if (inputGameID == gameID) {
//                wsFacade.makeMoveFacde(authToken, gameID, move);

                System.out.print(SET_TEXT_COLOR_GREEN);
                System.out.print("You resigned from the game.\n");
//            } else {
//                System.out.print("Error: Game ID does not match game joined\n");
//            }
//        } else {
//            System.out.print("Error: Did not provide game ID\n");
//        }
    }

    public void leave(String... params) throws ResponseException, DataAccessException, SQLException {
        if (params.length != 1) {
            int gameID=Integer.parseInt(params[1]);
            if (inputGameID == gameID) {
                if (!playerColor.equals("NULL")) {
                    new SQLGameDAO().updatePlayerColor(gameID, null, playerColor);
                }
//                    wsFacade=new WebSocketFacade(serverUrl, gameHandler);
                wsFacade.leaveFacade(authToken, gameID);

                System.out.print(SET_TEXT_COLOR_GREEN);
                System.out.print("Leaving game " + gameID + "...\n");
            } else {
                command="stay";
                System.out.print("Error: Game ID does not match game joined\n");
            }
        } else {
            command="stay";
            System.out.print("Error: Did not provide game ID\n");
        }
    }

    public void resign(String... params) {
        if (params.length != 1) {
            int gameID=Integer.parseInt(params[1]);

            if (inputGameID == gameID) {
                System.out.println("Are you sure you want to resign? (Yes/No)");
                System.out.print(">>> ");
                Scanner scanner=new Scanner(System.in);
                String input=scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    wsFacade.resignFacade(authToken, gameID);

                    System.out.print(SET_TEXT_COLOR_GREEN);
                    System.out.print("You resigned from the game.\n");
                }
            } else {
                System.out.print("Error: Game ID does not match game joined\n");
            }
        } else {
            System.out.print("Error: Did not provide game ID\n");
        }
    }

    public String highlight() {
        System.out.print(SET_TEXT_COLOR_GREEN);
        return String.format("Highlighting...\n");
    }
}
