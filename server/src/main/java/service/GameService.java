package service;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.UserData;
import org.junit.jupiter.api.MethodOrderer;
import service.*;

import static dataAccess.GameMemoryDAO.generateGameID;

public class GameService{
    public static List<GameData> listGames(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (AuthMemoryDAO.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        return new ArrayList<>(GameMemoryDAO.games.values());
    }
    public static Object createGame(GameData gameRecord, String authToken) throws ResponseException, DataAccessException {
        String gameName=gameRecord.gameName();
        AuthData username=AuthMemoryDAO.getAuth(authToken);
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (username == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (gameName == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        int gameID=generateGameID();
        GameData newGame=new GameData(gameID, gameRecord.whiteUsername(), gameRecord.blackUsername(), gameName, gameRecord.game());
        GameMemoryDAO.newGame(gameID, newGame);
        return Map.of("gameID", gameID);
    }

    public static Object joinGame(int gameID, String playerColor, String authToken) throws ResponseException, DataAccessException {
        GameData game = GameMemoryDAO.getGame(gameID);
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (AuthMemoryDAO.getAuth(authToken) == null) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        if (playerColor == null && gameID == 0) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (!GameMemoryDAO.games.containsKey(gameID)) {
            throw new ResponseException(400, "Error: bad request");
        }
        if (playerColor != null) {
            if ((playerColor.equals("WHITE") && game.whiteUsername() != null) ||
                    (playerColor.equals("BLACK") && game.blackUsername() != null)) {
                throw new ResponseException(403, "Error: already taken");
            }
            AuthData username=AuthMemoryDAO.getAuth(authToken);
            if (playerColor.equals("WHITE")) {
                GameData newGame = new GameData(game.gameID(), username.username(), game.blackUsername(), game.gameName(), game.game());
                GameMemoryDAO.updateGame(gameID, newGame);
            } else if (playerColor.equals("BLACK")) {
                GameData newGame = new GameData(game.gameID(), game.whiteUsername(), username.username(), game.gameName(), game.game());
                GameMemoryDAO.updateGame(gameID, newGame);
            }
        }
        if (playerColor == null) {
            if (game.whiteUsername() != null) {
                GameData newGame = new GameData(game.gameID(), playerColor, game.blackUsername(), game.gameName(), game.game());
                GameMemoryDAO.updateGame(gameID, newGame);
            } else {
                GameData newGame = new GameData(game.gameID(), game.whiteUsername(), playerColor, game.gameName(), game.game());
                GameMemoryDAO.updateGame(gameID, newGame);
            }
        }
        return Map.of("", "");
    }
}
