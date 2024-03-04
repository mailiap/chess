package passoffTests.serviceTests;

import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {
    @InjectMocks
    private GameService testGameDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        GameMemoryDAO.games.clear();
    }

    @Test
    void listGames_Postive() throws DataAccessException {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        GameMemoryDAO.newGame(1, testGame);
        assertDoesNotThrow(() -> {testGameDAO.listGames(authToken);
        });
    }

    @Test
    void listGames_Negative() {
        String invalidAuthToken = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> {testGameDAO.listGames(invalidAuthToken);
        });
    }

    @Test
    void createGame_Positive() throws DataAccessException {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = UserMemoryDAO.generateAuthToken(username);
        AuthMemoryDAO.createAuth(new AuthData(username, authToken));
        assertDoesNotThrow(() -> {testGameDAO.createGame(testGame, authToken);
        });
    }
//
    @Test
    void createGame_Negative() {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String invalidAuthToken = "invalidAuthToken";
        assertThrows(ResponseException.class, () -> {testGameDAO.createGame(testGame, invalidAuthToken);
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
        testGameDAO.joinGame(gameID, playerColor, authToken);
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
        testGameDAO.joinGame(1, invalidColor, authToken);
    }
}
