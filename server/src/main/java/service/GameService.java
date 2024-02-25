package service;

import dataAccess.GameDAO;
import model.GameData;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void createGame(GameData gameRecord) {
        // In a real-world scenario, you might validate game data before creating it
        gameDAO.createGame(gameRecord);
    }

    public GameData getGame(int gameId) {
        // Retrieve and return the game
        return gameDAO.getGame(gameId);
    }

    public List<GameData> listGames() {
        // Retrieve and return a list of all games
        return gameDAO.listGames();
    }

    public void updateGame(int gameId, GameData updatedGame) {
        // Update the game
        gameDAO.updateGame(gameId, updatedGame);
    }
}
