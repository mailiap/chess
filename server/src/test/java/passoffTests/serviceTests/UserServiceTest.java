package passoffTests.serviceTests;

import exception.ResponseException;
import model.*;
import dataAccess.*;
import service.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @InjectMocks
    private UserService testUserDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        UserMemoryDAO.users.clear();
    }

    @Test
    public void testRegister_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        AuthData authData =(AuthData) testUserDAO.register(userData);
        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testRegister_Negative() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        testUserDAO.register(userData);
        assertThrows(ResponseException.class, () -> testUserDAO.register(userData));
    }

    @Test
    public void testLogin_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        testUserDAO.register(userData);
        AuthData authData =(AuthData) testUserDAO.login(userData);
        assertNotNull(authData);
        assertNotNull(authData.authToken());
        assertEquals("testUser", authData.username());
    }

    @Test
    public void testLogin_Negative() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        testUserDAO.register(userData);
        UserData invalidCredentials = new UserData("testUser", "wrongPassword", "test@example.com");
        assertThrows(ResponseException.class, () -> testUserDAO.login(invalidCredentials));
    }

    @Test
    public void testLogout_Positive() throws ResponseException, DataAccessException {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        testUserDAO.register(userData);
        AuthData authData =(AuthData) testUserDAO.login(userData);
        testUserDAO.logout(authData.authToken());
        assertNull(UserMemoryDAO.getUser(authData.authToken()));
    }

    @Test
    public void testLogout_Negative() {
        UserData userData = new UserData("testUser", "password", "test@example.com");
        assertThrows(ResponseException.class, () -> testUserDAO.logout(userData.username()));

    }
}
