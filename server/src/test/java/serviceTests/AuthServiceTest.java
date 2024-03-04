package serviceTests;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testDeleteAllAuth_Postive() {
        AuthService.deleteAllAuth();

        assertTrue(UserMemoryDAO.users.isEmpty(), "Users map should be empty after deleting all auth");
        assertTrue(GameMemoryDAO.games.isEmpty(), "Games map should be empty after deleting all auth");
        assertTrue(AuthMemoryDAO.authTokens.isEmpty(), "Auth tokens map should be empty after deleting all auth");
    }
}
