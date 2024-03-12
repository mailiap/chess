package dataAccess;

import exception.ResponseException;
import model.AuthData;
import model.GameData;

import java.util.*;

public class GameMemoryDAO implements GameDAO {
    public static Map<Integer, GameData> gameData=new HashMap<>();
    private static int gameIdCounter=0;

    public void deleteAllGameData() {
        gameData.clear();
    }

    public Collection<GameData> getGames() {
//        if (gameData.size() != 1) {
//        Collection<GameData> gameList=new HashSet<>();
//        for (int i=gameID; i <= gameData.size(); i++) {
//            GameData game=gameData.get(i);
//            gameList.add(game);
//        }
//        return gameList;
//    }
        return gameData.values();
    }

    public int newGame(String gameName) throws DataAccessException {
        gameIdCounter++;
        if (!gameData.containsKey(gameIdCounter)) {
            GameData addGame=new GameData(gameIdCounter, null, null, gameName, null);
            gameData.put(gameIdCounter, addGame);
        } else {
            throw new DataAccessException("Game with ID " + gameIdCounter + " does not exist");
        }
        return gameIdCounter;
    }

    public GameData getGameByID(int gameId) {
        return gameData.get(gameId);
    }

    public void updatePlayerColor(int gameID, String username, String playerColor) throws DataAccessException, ResponseException {
        GameData oldGame=gameData.get(gameID);

        if (playerColor != null) {
            if ((playerColor.equals("WHITE") && oldGame.whiteUsername() != null) ||
                    (playerColor.equals("BLACK") && oldGame.blackUsername() != null)) {
                throw new ResponseException(403, "Error: already taken");
            }

            if (playerColor.equals("WHITE")) {
                GameData newGame=new GameData(oldGame.gameID(), username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
                gameData.put(gameID, newGame);
            } else if (playerColor.equals("BLACK")) {
                GameData newGame=new GameData(oldGame.gameID(), oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
                gameData.put(gameID, newGame);
            }
        }

        if (playerColor == null) {
            if (oldGame.whiteUsername() != null || oldGame.blackUsername() != null) {
                GameData newGame=new GameData(oldGame.gameID(), null, null, oldGame.gameName(), oldGame.game());
                gameData.put(gameID, newGame);
//            } else {
//                GameData newGame = new GameData(oldGame.gameID(), oldGame.whiteUsername(), playerColor, oldGame.gameName(), oldGame.game());
//                gameData.put(gameID, newGame);
//            }
            }
        }
    }
}
