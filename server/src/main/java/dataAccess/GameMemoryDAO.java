package dataAccess;

import model.GameData;
import java.util.HashMap;
import java.util.Map;

public class GameMemoryDAO implements GameDAO{
    public static Map<Integer, GameData> games=new HashMap<>();
    private static int gameIdCounter = 1;
    public static GameData getGame(int gameId) {
        return games.get(gameId);
    }
    public static void newGame (int gameId, GameData gameInfo) throws DataAccessException {
        // add new game to map
        if (!games.containsKey(gameId)) {
            games.put(gameId, gameInfo);
        } else {
            // throw exception
            throw new DataAccessException("Game with ID " + gameId + " does not exist");
        }
    }
    public static void updateGame (int gameId, GameData gameInfo) throws DataAccessException {
        // update game in map
        if (games.containsKey(gameId)) {
            games.put(gameId, gameInfo);
        } else {
            // throw exception
            throw new DataAccessException("Game with ID " + gameId + " does not exist");
        }
    }
    public static int generateGameID() {
        return gameIdCounter++;
    }
}
