package passoffTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.GameService;
import dataAccess.GameDAO;
import model.GameData;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {
    @Mock
    private GameDAO gameDAO;

    @InjectMocks
    private GameService gameService;

    private List<GameData> games;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGame_Positive() {
        GameData gameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        assertDoesNotThrow(() -> gameService.createGame(gameRecord));
    }

    @Test
    public void testGetGame_Positive() {
        GameData expectedGameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null);
        when(gameDAO.getGame(1)).thenReturn(expectedGameRecord);
        GameData result = gameService.getGame(1);
        assertNotNull(result);
        assertEquals(expectedGameRecord, result);
    }

    @Test
    public void testListGames_Positive() {
        when(gameDAO.listGames()).thenReturn(Collections.singletonList(new GameData(1, "whitePlayer", "blackPlayer", "Game 1", null)));
        List<GameData> result = gameService.listGames();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testUpdateGame_Positive() {
        GameData updatedGameRecord = new GameData(1, "whitePlayer", "blackPlayer", "Updated Game 1", null);
        assertDoesNotThrow(() -> gameService.updateGame(1, updatedGameRecord));
    }
}
