//package passoffTests.serviceTests;
//
//import model.*;
//import dataAccess.*;
//import service.*;
//import org.junit.jupiter.api.*;
//import org.mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class UserServiceTest {
//    @InjectMocks
//    private UserService testUserDAO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//
//    @Test
//    public void testCreateUser_Positive() {
//        UserData userData = new UserData("newUser", "password", "new@example.com");
//        testUserDAO.createUser(userData);
//        UserData createdUser = testUserDAO.getUser("newUser");
//        assertNotNull(createdUser);
//        assertEquals(userData.username(), createdUser.username());
//    }
//
//    @Test
//    public void testCreateUser_Negative() {
//        UserData userData = new UserData("existingUser", "password", "existing@example.com");
//        testUserDAO.createUser(userData);
//        assertThrows(RuntimeException.class, () -> testUserDAO.createUser(userData));
//    }
//
//    @Test
//    public void testGetUser_Positive() {
//        UserData userData = new UserData("newUser", "password", "new@example.com");
//        testUserDAO.createUser(userData);
//        UserData createdUser = testUserDAO.getUser("newUser");
//        assertNotNull(createdUser);
//        assertEquals(userData.username(), createdUser.username());
//    }
//
//    @Test
//    public void testGetUser_Negative() {
//        UserData user = testUserDAO.getUser("nonExistentUser");
//        assertNull(user);
//    }
//
//    @Test
//    public void testRegister_Positive() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        AuthData authData = testUserDAO.register(userData);
//        assertNotNull(authData);
//        assertNotNull(authData.authToken());
//        assertEquals("testUser", authData.username());
//    }
//
//    @Test
//    public void testRegister_Negative() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        testUserDAO.register(userData);
//        assertThrows(RuntimeException.class, () -> testUserDAO.register(userData));
//    }
//
//    @Test
//    public void testLogin_Positive() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        testUserDAO.register(userData);
//        Object authData = testUserDAO.login(userData);
//        assertNotNull(authData);
//        assertNotNull(authData.authToken());
//        assertEquals("testUser", authData.username());
//    }
//
//    @Test
//    public void testLogin_Negative() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        testUserDAO.register(userData);
//        UserData invalidCredentials = new UserData("testUser", "wrongPassword", "test@example.com");
//        assertThrows(RuntimeException.class, () -> testUserDAO.login(invalidCredentials));
//    }
//
//    @Test
//    public void testLogout_Positive() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        testUserDAO.register(userData);
//        AuthData authData = testUserDAO.login(userData);
//        testUserDAO.logout(userData);
//        assertNull(testUserDAO.getUser(authData.authToken()));
//    }
//
//    @Test
//    public void testLogout_Negative() {
//        UserData userData = new UserData("testUser", "password", "test@example.com");
//        assertDoesNotThrow(() -> testUserDAO.logout(userData));
//    }
//}
