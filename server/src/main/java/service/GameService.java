package service;

import dataAccess.*;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.*;

import model.UserData;
import org.junit.jupiter.api.MethodOrderer;
import service.*;

public class GameService {
    AuthDAO authMemory=new SQLAuthDAO();
    GameDAO gameMemory=new SQLGameDAO();

    public GameService() throws ResponseException, DataAccessException {
    }

    public List<GameData> listGames(String authToken) throws ResponseException {
        try {
            if (authToken == null || authToken.isEmpty()) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String username=authMemory.getUserByAuthToken(authToken);
            if (username == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            return new ArrayList<>(gameMemory.getGames());

        } catch (SQLException e) {
            throw new ResponseException(500, "Error: something went wrong");
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: something went wrong");
        }
    }

    public Object createGames(String authToken, String gameName) throws ResponseException {
        try {
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

            int gameID=gameMemory.newGame(gameName);
            return Map.of("gameID", gameID);

        } catch (SQLException e) {
            throw new ResponseException(500, "Error: something went wrong");
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: something went wrong");
        }
    }

    public Object joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        try {
            String username=authMemory.getUserByAuthToken(authToken);
            GameData oldGame=gameMemory.getGameByID(gameID);

            if (authToken == null || authToken.isEmpty()) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            if (username == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            if (playerColor == null && gameID == 0) {
                throw new ResponseException(400, "Error: bad request");
            }

            if (gameMemory.getGameByID(gameID) == null) {
                throw new ResponseException(400, "Error: bad request");
            }

            if (playerColor != null) {
                if ((playerColor.equals("WHITE") && oldGame.whiteUsername() != null) ||
                        (playerColor.equals("BLACK") && oldGame.blackUsername() != null)) {
                    throw new ResponseException(403, "Error: already taken");
                }
            }

            if (playerColor == null) {
                return Map.of("", "");
            }

            gameMemory.updatePlayerColor(gameID, username, playerColor);
            return Map.of("", "");

        } catch (SQLException e) {
            throw new ResponseException(500, "Error: something went wrong");
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: something went wrong");
        }
    }
}
