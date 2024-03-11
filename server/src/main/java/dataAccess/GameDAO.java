package dataAccess;

import exception.ResponseException;
import model.GameData;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO {
    void deleteAllGameData() throws DataAccessException, SQLException;
    Collection<GameData> getGames() throws DataAccessException, SQLException;
    int newGame(String username, String gameName) throws DataAccessException, SQLException;
    GameData getGameByID(int gameId) throws DataAccessException, SQLException;
    void updatePlayerColor(int gameID, String username, String playerColor) throws DataAccessException, SQLException, ResponseException;

}
