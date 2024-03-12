package serviceTests;

import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import spark.QueryParamsMap;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    UserService userSer = new UserService();
    UserMemoryDAO userMemory = new UserMemoryDAO();

    public UserServiceTest() throws ResponseException, SQLException, DataAccessException {
    }

    @BeforeEach
    public void setUp() {
        userMemory.userData.clear();
    }

    @Test
    public void testRegister_Positive() throws ResponseException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        AuthData authData =(AuthData) userSer.register(userData);

        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testRegister_Negative() throws ResponseException {
        UserData userData = new UserData("testUser", "password", "test@example.com");

        userSer.register(userData);

        assertThrows(ResponseException.class, () -> userSer.register(userData));
    }

    @Test
    public void testLogin_Positive() throws ResponseException, SQLException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        userSer.register(userData);
        AuthData authData =(AuthData) userSer.login(userData);

        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testLogin_Negative() throws ResponseException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        UserData invalidCredentials = new UserData("testUser", "wrongPassword", "test@example.com");

        userSer.register(userData);

        assertThrows(ResponseException.class, () -> userSer.login(invalidCredentials));
    }

    @Test
    public void testLogout_Positive() throws ResponseException, SQLException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");

        userSer.register(userData);
        AuthData authData =(AuthData) userSer.login(userData);
        userSer.logout(authData.authToken());

        assertNull(userMemory. getUserByUsername(authData.authToken()));
    }
    @Test
    public void testLogout_Negative() {
        UserData userData = new UserData("testUser", "password", "test@example.com");

        assertThrows(ResponseException.class, () -> userSer.logout(userData.username()));

    }
}
