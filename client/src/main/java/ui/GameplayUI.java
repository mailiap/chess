package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
    private State state=State.NOTRESIGNED;
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
        // save game after getting the load game notification
        // pass in game
        GameData gameData=new SQLGameDAO().getGameByID(inputGameID);

        if (playerColor.equals("BLACK")) {
            gameData.game().setTeamTurn(ChessGame.TeamColor.BLACK);
        }

        gameBoard.drawChessboard(gameData.game());
    }

    public void move(String... params) {
        if (params.length == 3) {
            int gameID=Integer.parseInt(params[1]);
            String newMove=params[2];
            String fromCol = newMove.substring(0, 1);
            int fromRow = Integer.parseInt(newMove.substring(1, 2));
            String toCol = newMove.substring(2, 3);
            int toRow = Integer.parseInt(newMove.substring(3, 4));


            ChessPosition startPosition = moveConverter(fromCol, fromRow);
            ChessPosition endPosition = moveConverter(toCol, toRow);
            ChessMove move = new ChessMove(startPosition, endPosition);

            if (inputGameID == gameID) {
                wsFacade.makeMoveFacde(authToken, gameID, move);

//                System.out.print(SET_TEXT_COLOR_GREEN);
//                System.out.print("You resigned from the game.\n");
            } else {
                System.out.print("Error: Game ID does not match game joined\n");
            }
        } else {
            System.out.print("Error: Did not provide game ID and/or move\n");
        }
    }

    public void leave(String... params) throws ResponseException, DataAccessException, SQLException {
        if (params.length != 1) {
            int gameID=Integer.parseInt(params[1]);
            if (inputGameID == gameID) {
                if (playerColor != null) {
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
                System.out.println(SET_TEXT_COLOR_YELLOW + "Are you sure you want to resign? (Yes/No)");
                System.out.print(RESET_TEXT_COLOR+ "[GAMEPLAY] >>> ");
                Scanner scanner=new Scanner(System.in);
                String input=scanner.nextLine().trim().toLowerCase();
                if (input.equals("yes")) {
                    wsFacade.resignFacade(authToken, gameID);
                    state = State.RESIGNED;
                    System.out.print(SET_TEXT_COLOR_GREEN + "You resigned from the game.\n");
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

//    public String moveCoverterToString(ChessMove move) {
//        ChessPosition startPosition = move.getStartPosition();
//        ChessPosition endPosition = move.getEndPosition();
//        return moveConverter(startPosition.getColumn() + startPosition.getRow() + moveConverter(endPosition.getColumn()) + endPosition.getRow());
//    }

    public ChessPosition moveConverter(String rowToConvert, int col) {
        int convertedCol=0;
        switch (rowToConvert) {
            case "a" -> convertedCol = 1;
            case "b" -> convertedCol = 2;
            case "c" -> convertedCol = 3;
            case "d" -> convertedCol = 4;
            case "e" -> convertedCol = 5;
            case "f" -> convertedCol = 6;
            case "g" -> convertedCol = 7;
            case "h" -> convertedCol = 8;
        }
        return new ChessPosition(col, convertedCol);
        // convert string into a int
        // set new postion inside of new move using the converted col
    }
}
