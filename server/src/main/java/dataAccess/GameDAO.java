package dataAccess;

import model.GameData;

public interface GameDAO {
static void getGame(int gameId) throws DataAccessException {};
static void updateGame(int gameId, GameData updatedGame) throws DataAccessException {};
}
