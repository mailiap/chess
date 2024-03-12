package serviceTests;
import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    AuthService authSer = new AuthService();
    AuthMemoryDAO authMemory = new AuthMemoryDAO();
    UserMemoryDAO userMemory = new UserMemoryDAO();
    GameMemoryDAO gameMemory = new GameMemoryDAO();

    public AuthServiceTest() throws ResponseException, SQLException, DataAccessException {
    }


    @BeforeEach
    public void setUp() {
        authMemory.authData.clear();
    }

    @Test
    public void testDeleteAllAuth_Postive() throws ResponseException {
        authSer.clearDatabase();

        assertTrue(userMemory.userData.isEmpty(), "Users map should be empty after deleting all auth");
        assertTrue(gameMemory.gameData.isEmpty(), "Games map should be empty after deleting all auth");
        assertTrue(authMemory.authData.isEmpty(), "Auth tokens map should be empty after deleting all auth");
    }
}
