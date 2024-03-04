package passoffTests.serviceTests;

import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {
    @InjectMocks
    private AuthService testAuthDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testDeleteAllAuth_Postive() {
        testAuthDAO.deleteAllAuth();
        assertTrue(UserMemoryDAO.users.isEmpty(), "Users map should be empty after deleting all auth");
        assertTrue(GameMemoryDAO.games.isEmpty(), "Games map should be empty after deleting all auth");
        assertTrue(AuthMemoryDAO.authTokens.isEmpty(), "Auth tokens map should be empty after deleting all auth");
    }
}
