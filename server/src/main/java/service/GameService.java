package service;

import dataAccess.GameDAO;
import model.GameData;
import java.util.List;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public GameData createGame(GameData gameRecord) {
        return gameDAO.createGame(gameRecord);
    }

    public GameData getGame(int gameId) {
        return gameDAO.getGame(gameId);
    }

    public List<GameData> listGames() {
        return gameDAO.listGames();
    }

    public void updateGame(int gameId, GameData updatedGame) {
        gameDAO.updateGame(gameId, updatedGame);
    }
}
