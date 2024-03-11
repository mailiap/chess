package service;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import java.io.Serializable;
import java.util.*;

import model.UserData;
import org.junit.jupiter.api.MethodOrderer;
import service.*;

public class GameService{
    AuthMemoryDAO authMemory = new AuthMemoryDAO();
    GameMemoryDAO gameMemory = new GameMemoryDAO();

    public List<GameData> listGames(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        String username = authMemory.getUserByAuthToken(authToken);
        if (username == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        return new ArrayList<>(gameMemory.getGames());


    }

    public Object createGames(String authToken, String gameName) throws ResponseException, DataAccessException {
        String username=authMemory.getUserByAuthToken(authToken);

        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        if (username == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        if (gameName == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        int gameID = gameMemory.newGame(username, gameName);
        return Map.of("gameID", gameID);
    }

    public Object joinGame(String authToken, String playerColor, int gameID) throws ResponseException, DataAccessException {
        String username = authMemory.getUserByAuthToken(authToken);

        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        if (username == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        if (playerColor == null && gameID == 0) {
            throw new ResponseException(400, "Error: bad request");
        }

        if (!gameMemory.gameData.containsKey(gameID)) {
            throw new ResponseException(400, "Error: bad request");
        }

        gameMemory.updatePlayerColor(gameID, username, playerColor);
        return Map.of("", "");
    }
}
