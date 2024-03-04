package serviceTests;

import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @BeforeEach
    public void setUp() {
        UserMemoryDAO.users.clear();
    }

    @Test
    public void testRegister_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        AuthData authData =(AuthData) UserService.register(userData);

        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testRegister_Negative() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");

        UserService.register(userData);

        assertThrows(ResponseException.class, () -> UserService.register(userData));
    }

    @Test
    public void testLogin_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        UserService.register(userData);
        AuthData authData =(AuthData) UserService.login(userData);

        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testLogin_Negative() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        UserData invalidCredentials = new UserData("testUser", "wrongPassword", "test@example.com");

        UserService.register(userData);

        assertThrows(ResponseException.class, () -> UserService.login(invalidCredentials));
    }

    @Test
    public void testLogout_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        AuthData authData =(AuthData) UserService.login(userData);

        UserService.register(userData);
        UserService.logout(authData.authToken());

        assertNull(UserMemoryDAO.getUser(authData.authToken()));
    }

    @Test
    public void testLogout_Negative() {
        UserData userData = new UserData("testUser", "password", "test@example.com");

        assertThrows(ResponseException.class, () -> UserService.logout(userData.username()));
    }
}
