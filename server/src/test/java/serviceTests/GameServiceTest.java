package serviceTests;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    @BeforeEach
    public void setUp() {
        GameMemoryDAO.games.clear();
    }
    @Test
    void listGames_Postive() throws DataAccessException {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        GameMemoryDAO.newGame(1, testGame);
        assertDoesNotThrow(() -> {GameService.listGames(authToken);
        });
    }
    @Test
    void listGames_Negative() {
        String invalidAuthToken = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> {GameService.listGames(invalidAuthToken);
        });
    }
    @Test
    void createGame_Positive() throws DataAccessException {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        assertDoesNotThrow(() -> {GameService.createGame(testGame, authToken);
        });
    }
    @Test
    void createGame_Negative() {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String invalidAuthToken = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> {GameService.createGame(testGame, invalidAuthToken);
        });
    }
    @Test
    void joinGame_Postive() throws ResponseException, DataAccessException {
        int gameID = 1;
        String playerColor = "WHITE";
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        GameData testGame = new GameData(1, null, null, "gameName", null);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        GameMemoryDAO.newGame(gameID, testGame);
        GameService.joinGame(gameID, playerColor, authToken);
        GameData updatedGameData = GameMemoryDAO.getGame(gameID);
        assertEquals(username, updatedGameData.whiteUsername());
    }
    @Test
    void joinGame_Negative() throws ResponseException, DataAccessException {
        String invalidColor = "INVALID_COLOR";
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        GameMemoryDAO.newGame(1, testGame);
        GameService.joinGame(1, invalidColor, authToken);
    }
}
