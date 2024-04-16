package ui;

import chess.ChessBoard;
import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerFacade;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String username=null;
    private String password=null;
    private String authToken=null;
    private GameData newGame;
    private State state=State.SIGNEDOUT;
    private ServerFacade server;
    private String serverUrl;
    private GameHandler gameHandler;
    private WebSocketFacade wsFacade;


    public ChessClient(String serverUrl, GameHandler gameHandler) throws ResponseException {
        server=new ServerFacade(serverUrl);
        this.serverUrl=serverUrl;
        this.gameHandler=gameHandler;
//        wsFacade = new WebSocketFacade(serverUrl, gameHandler);
    }

    public String eval(String input) {
        try {
            var tokens=input.toLowerCase().split(" ");
            var choice=(tokens.length > 0) ? tokens[0] : "help";
            var params=Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state == State.SIGNEDOUT) {
                return switch (choice) {
                    case "register" -> register(params);
                    case "login" -> login(params);
                    case "quit" -> quit();
                    case "help" -> help();
                    default -> throw new IllegalStateException("Unexpected value: " + choice + "\n");
                };
            } else {
                return switch (choice) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> joinObserver(params);
                    case "logout" -> logout();
                    case "quit" -> quit();
                    case "help" -> help();
                    default -> throw new IllegalStateException("Unexpected value: " + choice + "\n");
                };
            }
        } catch (ResponseException | IOException ex) {
            if (ex.getMessage() == "Already connected") {
                state=State.SIGNEDIN;
            }
            return String.format(ex.getMessage() + "\n");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String register(String... params) throws ResponseException, IOException, URISyntaxException {
        username=params[0];
        password=params[1];
        String email=params[2];
        UserData user=new UserData(username, password, email);
        AuthData auth=server.register(user);
        authToken=auth.authToken();
        if (user != null) {
            login(username, password);
        }
        return String.format(SET_TEXT_COLOR_GREEN + "logged in as %s.\n", auth.username());
    }

    public String login(String... params) throws ResponseException {
        username=params[0];
        password=params[1];
        UserData user=new UserData(username, password, null);
        AuthData auth=server.login(user);
        authToken=auth.authToken();
        state=State.SIGNEDIN;
        return String.format(SET_TEXT_COLOR_GREEN + "You signed in as %s.\n", auth.username());
    }

    public String createGame(String... params) throws ResponseException {
        String gameName=params[0];
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame chessGame = new ChessGame();
        chessGame.setBoard(board);
        newGame=new GameData(0, null, null, gameName, chessGame);
        server.createGame(newGame);
        return String.format(SET_TEXT_COLOR_GREEN + "You created game %s.\n", newGame.gameName());
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        GameData[] games=server.listGames();
        StringBuilder result=new StringBuilder();
        if (games.length == 0) {
            result.append("No games available.\n");
        } else {
            System.out.print(SET_TEXT_COLOR_YELLOW);
            result.append("Games:\n");
            for (int i=0; i < games.length; i++) {
                if (games[i].whiteUsername() == null && games[i].blackUsername() != null) {
                    result.append(SET_TEXT_COLOR_YELLOW + (i + 1)).append(". ").append(games[i].gameName()).append("\n\t").append(SET_TEXT_COLOR_YELLOW + "white user is empty").append(SET_TEXT_COLOR_RED + "\n\tblack user is ").append("'" + games[i].blackUsername() + "'");
                } else if (games[i].whiteUsername() != null && games[i].blackUsername() == null) {
                    result.append(SET_TEXT_COLOR_YELLOW + (i + 1)).append(". ").append(games[i].gameName()).append("\n\t").append(SET_TEXT_COLOR_BLUE + "white user is ").append("'" + games[i].whiteUsername() + "'").append(SET_TEXT_COLOR_YELLOW + "\n\tblack user is empty");
                } else if (games[i].whiteUsername() == null && games[i].blackUsername() == null) {
                    result.append(SET_TEXT_COLOR_YELLOW + (i + 1)).append(". ").append(games[i].gameName()).append("\n\t").append(SET_TEXT_COLOR_YELLOW + "white user is empty").append(SET_TEXT_COLOR_YELLOW + "\n\tblack user is empty");
                } else {
                    result.append(SET_TEXT_COLOR_YELLOW + (i + 1)).append(". ").append(games[i].gameName()).append("\n\t").append(SET_TEXT_COLOR_BLUE + "white user is ").append("'" + games[i].whiteUsername() + "'").append(SET_TEXT_COLOR_RED + "\n\tblack user is ").append("'" + games[i].blackUsername() + "'");
                }
                if (i == games.length - 1) {
                    result.append("\n");
                } else {
                    result.append("\n\n");
                }
            }
        }
        return SET_TEXT_COLOR_YELLOW + result;
    }

    public String joinGame(String... params) throws ResponseException, SQLException, DataAccessException {
        int gameID=Integer.parseInt(params[0]);
        String playerColor=params[1].toUpperCase();
        server.joinGame(gameID, playerColor);
        wsFacade=new WebSocketFacade(serverUrl, gameHandler);
        wsFacade.joinPlayerFacade(authToken, gameID, ChessGame.TeamColor.valueOf(playerColor));
         return "";
    }

    public String joinObserver(String... params) throws ResponseException, SQLException, DataAccessException {
        int gameID=Integer.parseInt(params[0]);
        server.joinGame(gameID, null);
        wsFacade=new WebSocketFacade(serverUrl, gameHandler);
        wsFacade.joinObserverFacade(authToken, gameID);
        return "";
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout();
        state=State.SIGNEDOUT;
        System.out.print(SET_TEXT_COLOR_GREEN);
        return String.format("%s has been logged out.\n", username);
    }

    public String quit() {
        state=State.SIGNEDOUT;
        System.out.print(SET_TEXT_COLOR_GREEN);
        return String.format("Exiting program...");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """ 
                    register <USERNAME> <PASSWORD> <EMAIL>
                    login <USERNAME> <PASSWORD>
                    quit
                    help
                    """;
        }
        return """
                create ‹NAME>
                list
                join <ID> [WHITE|BLACK|<empty>]
                observe ‹ID>
                logout
                quit
                help
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
