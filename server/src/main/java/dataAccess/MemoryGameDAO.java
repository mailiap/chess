package dataAccess;

import chess.ChessGame;
import model.GameData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> games=new HashMap<>();

    @Override
    public GameData createGame(GameData gameRecord) {
        int game=gameRecord.gameID();
        if (games.containsKey(game)) {
            throw new RuntimeException("Game '" + game + "' already exists");
        }
        return games.put(game, gameRecord);
    }

    @Override
    public GameData getGame(int gameId) {
        return games.get(gameId);
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(int gameId, GameData updatedGame) {
        if (games.containsKey(gameId)) {
            games.put(gameId, updatedGame);
        } else {
            // Handle case where gameId does not exist
            throw new IllegalArgumentException("Game with ID " + gameId + " does not exist");
        }
    }
}
