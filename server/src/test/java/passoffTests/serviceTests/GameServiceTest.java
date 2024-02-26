package passoffTests.serviceTests;

import dataAccess.MemoryGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    private MemoryGameDAO testGameDAO;

    @BeforeEach
    public void setUp() {
        testGameDAO = new MemoryGameDAO();
    }

    @Test
    public void testCreateGame_Positive() {
        GameData gameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        assertDoesNotThrow(() -> testGameDAO.createGame(gameRecord));
    }

    @Test
    public void testCreateGame_Negative() {
        GameData gameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        testGameDAO.createGame(gameRecord);
        assertThrows(RuntimeException.class, () -> testGameDAO.createGame(gameRecord));
    }

    @Test
    public void testGetGame_Positive() {
        GameData expectedGameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        testGameDAO.createGame(expectedGameRecord);
        GameData result = testGameDAO.getGame(1);
        assertNotNull(result);
        assertEquals(expectedGameRecord, result);
    }

    @Test
    public void testGetGame_Negative() {
        GameData result = testGameDAO.getGame(1);
        assertNull(result);
    }

    @Test
    public void testListGames_Positive() {
        GameData gameRecord1 = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        GameData gameRecord2 = new GameData(2, "whitePlayer", "blackPlayer", "Game 2", null);
        testGameDAO.createGame(gameRecord1);
        testGameDAO.createGame(gameRecord2);
        List<GameData> games = testGameDAO.listGames();
        assertNotNull(games);
        assertEquals(2, games.size());
    }

    @Test
    public void testListGames_Negative() {
        List<GameData> games = testGameDAO.listGames();
        assertNotNull(games);
        assertEquals(0, games.size());
    }

    @Test
    public void testUpdateGame_Positive() {
        GameData originalGameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        testGameDAO.createGame(originalGameRecord);
        GameData updatedGameRecord = new GameData(1, "updatedWhitePlayer", "updatedBlackPlayer", "Updated Game 1", null);
        testGameDAO.updateGame(1, updatedGameRecord);
        GameData result = testGameDAO.getGame(1);
        assertNotNull(result);
        assertEquals(updatedGameRecord, result);
    }

    @Test
    public void testUpdateGame_Negative() {
        GameData updatedGameRecord = new GameData(1, "updatedWhitePlayer", "updatedBlackPlayer", "Updated Game 1", null);
        assertThrows(RuntimeException.class, () -> testGameDAO.updateGame(1, updatedGameRecord));
    }
}
