package service;

import dataAccess.GameDAO;
import exception.ResponseException;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.UserData;
import service.*;

import static service.UserService.authTokens;

public class GameService implements GameDAO {
    public static Map<Integer, GameData> games=new HashMap<>();

    public static Object createGame(GameData gameRecord, String authToken) throws ResponseException {
        // Check if authToken is provided
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        // Check if the gameName is empty
        if (gameRecord.gameName().isEmpty()) {
            throw new ResponseException(400, "Error: bad request" );
        }
        // Check if the gameName is already taken
        for (GameData existingGame : games.values()) {
            if (existingGame.gameName().equals(gameRecord.gameName())) {
                throw new ResponseException(400, "Error: bad request" );
            }
        }
        // Generate a new gameID
        int gameID=gameRecord.gameID();
        // Create the new game
        games.put(gameID, gameRecord);
        // Return the gameID of the newly created game
        return Map.of("gameID", gameID);
    }

    public GameData getGame(int gameId) {
        return games.get(gameId);
    }

    public static List<GameData> listGames(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (!authTokens.containsKey(authToken)) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return new ArrayList<>(games.values());
    }

    public void updateGame(int gameId, GameData updatedGame) {
        if (games.containsKey(gameId)) {
            games.put(gameId, updatedGame);
        } else {
            throw new IllegalArgumentException("Game with ID " + gameId + " does not exist");
        }
    }

    public static Object joinGame(int gameID, String playerColor, String authToken) throws ResponseException {
        // Check if authToken is provided
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        // Check if the playerColor and gameID are provided
        if (playerColor == null || playerColor.isEmpty() || gameID == 0) {
            throw new ResponseException(400, "Error: bad request");
        }

        // Check if the game exists
        if (!games.containsKey(gameID)) {
            throw new ResponseException(400, "Error: bad request");
        }

        // Check if the playerColor is valid (WHITE or BLACK)
        playerColor = playerColor.toUpperCase();
        if (!playerColor.equals("WHITE") && !playerColor.equals("BLACK")) {
            throw new ResponseException(400, "Error: bad request");
        }

        // Get the game data
        GameData game = games.get(gameID);

        // Check if the game already has a player with the specified color
        if ((playerColor.equals("WHITE") && game.whiteUsername() != null) ||
                (playerColor.equals("BLACK") && game.blackUsername() != null)) {
            throw new ResponseException(403, "Error: already taken");
        }
        // Add the caller as the requested color to the game or as an observer if no color is specified
//        String username = authTokens.get(authToken).username();
        if (playerColor.equals("WHITE")) {
            game.whiteUsername();
        } else if (playerColor.equals("BLACK")) {
            game.blackUsername();
        }
        // Return success response
        return Map.of("", "");
    }
}
