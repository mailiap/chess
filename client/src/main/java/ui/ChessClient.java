package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerFacade;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String username = null;
    private String password = null;
    private ServerFacade server;
    private String serverUrl;
    private State state = State.SIGNEDOUT;
    private Scanner scanner;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.scanner = new Scanner(System.in);

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
                state = State.SIGNEDIN;
            }
            return String.format(ex.getMessage() + "\n");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String register(String... params) throws ResponseException, IOException, URISyntaxException {
            username=params[0];
            password=params[1];
            String email=params[2];
            UserData user = new UserData(username, password, email);
            AuthData auth = server.register(user);
            if (user != null) {
                login(username, password);
            }
            return String.format(SET_TEXT_COLOR_GREEN + "logged in as %s.\n", auth.username());
        }

    public String login(String... params) throws ResponseException {
            username = params[0];
            password = params[1];
            UserData user = new UserData(username, password, null);
            AuthData auth = server.login(user);
            state = State.SIGNEDIN;
            return String.format(SET_TEXT_COLOR_GREEN + "You signed in as %s.\n", auth.username());
        }

    public String createGame(String... params) throws ResponseException {
            String gameName=params[0];
            GameData newGame = new GameData(0, null, null, gameName, null);
            server.createGame(newGame);
            return String.format(SET_TEXT_COLOR_GREEN + "You created game %s.\n", newGame.gameName());
        }

    public String listGames() throws ResponseException {
        assertSignedIn();
        GameData[] games = server.listGames();
        StringBuilder result = new StringBuilder();
        if (games.length == 0) {
            result.append("No games available.");
        } else {
            result.append("Games:\n");
            for (int i = 0; i < games.length; i++) {
                result.append((i + 1)).append(". ").append(games[i].gameName()).append('\n');
            }
        }
        return SET_TEXT_COLOR_YELLOW + result;
    }
    
    public String joinGame(String... params) throws ResponseException {
        int gameID = Integer.parseInt(params[0]);
        String playerColor = params[1].toUpperCase();
        server.joinGame(gameID, playerColor);
        new GameplayUI().run();

        if (playerColor.equals("WHITE")) {
            System.out.println(SET_TEXT_COLOR_BLUE);
        } else {
            System.out.println(SET_TEXT_COLOR_RED);
        }
        return String.format("\nuser %s joined game %d on the %s team.\n", username, gameID, playerColor);
    }

    public String joinObserver(String... params) throws ResponseException {
        int gameID = Integer.parseInt(params[0]);
        server.joinGame(gameID, null);
        new GameplayUI().run();
        System.out.println(SET_TEXT_COLOR_YELLOW);
        return String.format("\njoined game %d as an observer.\n", gameID);
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        server.logout();
        state = State.SIGNEDOUT;
        System.out.println(SET_TEXT_COLOR_GREEN);
        return String.format("%s has been logged out.\n", username);
    }

    public String quit() {
        state=State.SIGNEDOUT;
        System.out.println(SET_TEXT_COLOR_GREEN);
        return String.format("Exiting program...\n");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """ 
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """;
        }
        return """
                create ‹NAME> - a game
                list - games
                join <ID> [WHITE|BLACK|<empty>] - a game
                observe ‹ID> - a game
                logout - when you are done 
                quit - playing chess
                help - with possible commands
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
