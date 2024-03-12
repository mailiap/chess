package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.GameService;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDAOTest {

    private SQLGameDAO SQLGame;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException, ResponseException {
        SQLGame = new SQLGameDAO();
    }

    @AfterEach
    public void tearDown() throws SQLException, DataAccessException {
        SQLGame.deleteAllGameData();
    }

    @Test
    public void testDeleteAllGameData_Positive() throws DataAccessException, SQLException {
        int gameID1 = SQLGame.newGame( "game1");
        int gameID2 = SQLGame.newGame( "game2");

        SQLGame.deleteAllGameData();

        assertNull(SQLGame.getGameByID(gameID1));
        assertNull(SQLGame.getGameByID(gameID2));
    }

    @Test
    public void testDeleteAllGameData_Negative() throws ResponseException, DataAccessException, SQLException {
        SQLGameDAO SQLGame = new SQLGameDAO() {
            @Override
            public void deleteAllGameData() throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, SQLGame::deleteAllGameData);
    }

    @Test
    public void testGetGames_Positive() throws DataAccessException, SQLException {
        SQLGame.newGame("game1");
        SQLGame.newGame("game2");

        Collection<GameData> games = SQLGame.getGames();

        assertEquals(2, games.size());
    }

    @Test
    public void testGetGames_Negative() throws ResponseException, DataAccessException {
        SQLGameDAO gameDAO = new SQLGameDAO() {
            @Override
            public Collection<GameData> getGames() throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, gameDAO::getGames);
    }

    @Test
    public void testNewGame_Positive() throws DataAccessException, SQLException {
        String username = "testuser";
        String gameName = "Test Game";

        int result = SQLGame.newGame(gameName);

        assertEquals(1, result);
    }

    @Test
    public void testNewGame_Negative() throws SQLException, DataAccessException {
        String gameName = "Test Game";

        int gameId = SQLGame.newGame(gameName);

        assertTrue(gameId > 0);
    }

    @Test
    public void testGetGameByID_Positive() throws SQLException, DataAccessException {
        int gameID = SQLGame.newGame("Test Game");

        SQLGame.updatePlayerColor(gameID, "whiteUsername", "WHITE");
        SQLGame.updatePlayerColor(gameID, "blackUsername", "BLACK");

        GameData retrievedGameData = SQLGame.getGameByID(gameID);

        assertNotNull(retrievedGameData);
        assertEquals(1, retrievedGameData.gameID());
        assertEquals("whiteUsername", retrievedGameData.whiteUsername());
        assertEquals("blackUsername", retrievedGameData.blackUsername());
        assertEquals("Test Game", retrievedGameData.gameName());
    }

    @Test
    public void testGetGameByID_Negative() throws SQLException, DataAccessException {
        int gameID = SQLGame.newGame("Test Game");

        assertNull(SQLGame.getGameByID(gameID + 1));
    }

    @Test
    public void testUpdatePlayerColor_Positive() throws DataAccessException, SQLException {
        int gameID = SQLGame.newGame("Test Game");
        GameData expectedGameData = new GameData(gameID, "whiteUsername", null, "Test Game", null);

        SQLGame.updatePlayerColor(gameID, "whiteUsername", "WHITE");

        GameData retrievedGameData = SQLGame.getGameByID(gameID);

        assertNotNull(retrievedGameData);
        assertEquals(expectedGameData.gameID(), retrievedGameData.gameID());
        assertEquals(expectedGameData.whiteUsername(), retrievedGameData.whiteUsername());
        assertEquals(expectedGameData.blackUsername(), retrievedGameData.blackUsername());
        assertEquals(expectedGameData.gameName(), retrievedGameData.gameName());
    }

    @Test
    public void testUpdatePlayerColor_Negative() throws ResponseException, DataAccessException, SQLException {
        SQLGameDAO SQLGame = new SQLGameDAO() {
            @Override
            public void updatePlayerColor(int gameID, String username, String playerColor) throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, () -> SQLGame.updatePlayerColor(1, "testuser", "Test Game"));
    }

}
