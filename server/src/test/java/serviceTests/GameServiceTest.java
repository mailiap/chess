package serviceTests;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTest {

    public GameService gameSer = new GameService();
    public AuthMemoryDAO authMemory = new AuthMemoryDAO();
    public GameMemoryDAO gameMemory = new GameMemoryDAO();

    public GameServiceTest() throws ResponseException, DataAccessException {
    }

    @BeforeEach
    public void setUp() {
        gameMemory.gameData.clear();
    }

    @Test
    void listGames_Postive() throws DataAccessException {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = authMemory.createAuthToken(username);

        gameMemory.newGame(testGame.gameName());

        assertDoesNotThrow(() -> {gameSer.listGames(authToken);
        });
    }

    @Test
    void listGames_Negative() {
        String invalidAuthToken = "invalidAuthToken";

        assertThrows(ResponseException.class, () -> {gameSer.listGames(invalidAuthToken);
        });
    }

    @Test
    void createGame_Positive() {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String username = "username";
        String authToken = authMemory.createAuthToken(username);

        assertDoesNotThrow(() -> {gameSer.createGames(authToken, testGame.gameName());
        });
    }

    @Test
    void createGame_Negative() {
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String invalidAuthToken = "invalidAuthToken";

        assertThrows(ResponseException.class, () -> {gameSer.createGames(invalidAuthToken, testGame.gameName());
        });
    }

    @Test
    void joinGame_Postive() throws DataAccessException {
        int gameID = 1;
        String playerColor = "WHITE";
        String username = "username";
        GameData testGame = new GameData(1, null, null, "gameName", null);

        String authToken = authMemory.createAuthToken(username);
        gameMemory.newGame(testGame.gameName());

        assertThrows(ResponseException.class, () -> {gameSer.joinGame(authToken, playerColor, 1);
        });
    }

    @Test
    void joinGame_Negative() throws DataAccessException {
        String invalidColor = "INVALID_COLOR";
        String username = "username";
        GameData testGame = new GameData(1, "whiteUsername", "blackUsername", "gameName", null);
        String authToken = authMemory.createAuthToken(username);

        gameMemory.newGame(testGame.gameName());

        assertThrows(ResponseException.class, () -> {gameSer.joinGame(authToken, invalidColor, 1);
        });
    }
}
