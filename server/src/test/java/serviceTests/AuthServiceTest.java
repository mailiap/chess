package serviceTests;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    AuthService authSer = new AuthService();
    AuthMemoryDAO authMemory = new AuthMemoryDAO();
    UserMemoryDAO userMemory = new UserMemoryDAO();
    GameMemoryDAO gameMemory = new GameMemoryDAO();


    @BeforeEach
    public void setUp() {
        authMemory.authData.clear();
    }

    @Test
    public void testDeleteAllAuth_Postive() {
        authSer.clearDatabase();

        assertTrue(userMemory.userData.isEmpty(), "Users map should be empty after deleting all auth");
        assertTrue(gameMemory.gameData.isEmpty(), "Games map should be empty after deleting all auth");
        assertTrue(authMemory.authData.isEmpty(), "Auth tokens map should be empty after deleting all auth");
    }
}
