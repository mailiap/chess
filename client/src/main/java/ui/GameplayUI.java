package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import exception.ResponseException;
import model.GameData;
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

public class GameplayUI implements GameHandler {
    private String playerColor = "";
    private int inputGameID = 0;
    private String command = "";
    private String authToken = "";
    private final Scanner scanner;
    private DrawGameBoard gameBoard;
    private String serverUrl;
    private GameHandler gameHandler;
    private WebSocketFacade wsFacade;

    public GameplayUI(String serverUrl, GameHandler gameHandler) {
        this.scanner = new Scanner(System.in);
        this.gameBoard = new DrawGameBoard();
        this.serverUrl=serverUrl;
        this.gameHandler=gameHandler;
    }

    public String run(String input) throws ResponseException, SQLException, DataAccessException {
        var inputTokens=input.toLowerCase().split(" ");
        var inputParams=Arrays.copyOfRange(inputTokens, 0, inputTokens.length);

        inputGameID =Integer.parseInt(inputParams[0]);
        if (inputParams.length < 3) {
            playerColor = inputParams[1].toUpperCase();
        } else {
            authToken = inputParams[1];
            playerColor = inputParams[2].toUpperCase();
        }


        redraw(playerColor);

        System.out.print("\n" + RESET_TEXT_COLOR + help());
//        var command = "";
        while (!command.contains("leave")) {

            System.out.print("\n" + RESET_TEXT_COLOR + ">>> ");
            command=scanner.nextLine().trim().toLowerCase();
            var tokens=command.toLowerCase().split(" ");
            var choice=(tokens.length > 0) ? tokens[0] : "help";
            var params=Arrays.copyOfRange(tokens, 0, tokens.length);

            switch (choice) {
                case "help" -> System.out.print(help());
                case "redraw" -> System.out.print(redraw(playerColor));
                case "leave" -> System.out.print(leave(params));
                case "move" -> System.out.print(move());
                case "resign" -> System.out.print(resign());
                case "highlight" -> System.out.print(highlight());
                default -> System.out.print("Unexpected value: " + command + "\n");
            }
        }
        return "";
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

    public String redraw(String playerColor) {
        boolean teamTurn = false;

        if (playerColor.equals("WHITE") || playerColor.equals("NULL")) {
            teamTurn = true;
        }

        var out = new PrintStream(System.out, teamTurn, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        gameBoard.drawChessboard(out, teamTurn);

        gameBoard.drawHeader(out);

        if (playerColor.equals("WHITE")) {
            System.out.println(SET_TEXT_COLOR_BLUE);
            System.out.println(String.format("You are on the " + playerColor + " team"));

        }
        else if (playerColor.equals("BLACK")){
            System.out.println(SET_TEXT_COLOR_RED);
            System.out.println(String.format("You are on the " + playerColor + " team"));

        }
        else if (playerColor.equals("NULL")){
            System.out.println(SET_TEXT_COLOR_YELLOW);
            System.out.println(String.format("You are an observer."));
        }

        return "";
    }

    public String leave(String... params) throws ResponseException, DataAccessException, SQLException {
        if (params.length != 1) {
            int gameID =Integer.parseInt(params[1]);

            if (inputGameID == gameID) {
                if (!authToken.isEmpty()) {
                    new SQLGameDAO().updatePlayerColor(gameID, null, playerColor);
                    wsFacade=new WebSocketFacade(serverUrl, gameHandler);
                    wsFacade.leaveFacade(authToken, gameID);
                }

                System.out.print(SET_TEXT_COLOR_GREEN);
                return String.format("Leaving game " + gameID + "...\n");
            } else {
                command = "stay";
                return "Error: Game ID does not match game joined\n";
            }
        } else {
            command = "stay";
            return "Error: Did not provide game ID\n";
        }
    }

    public String move() {
        System.out.print(SET_TEXT_COLOR_GREEN);
        return String.format("Moving...\n");
    }

    public String resign() {
        System.out.println("Are you sure you want to resign? (Yes/No)");
        String input = scanner.nextLine().trim().toLowerCase();
        if (input.equals("yes")) {
            System.out.println("You resigned from the game.");
        } else {
            System.out.println("Resignation cancelled.");
        }
        return "";
    }

    public String highlight() {
        System.out.print(SET_TEXT_COLOR_GREEN);
        return String.format("Highlighting...\n");
    }

    @Override
    public void updateGame(ChessGame game) {
        String gameInfo = new Gson().toJson(game);
    }

    @Override
    public void printMessage(ServerMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message.toString());

    }
}
