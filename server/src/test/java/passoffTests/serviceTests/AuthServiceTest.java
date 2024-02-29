//package passoffTests.serviceTests;
//
//import model.*;
//import dataAccess.*;
//import service.*;
//import org.junit.jupiter.api.*;
//import org.mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class AuthServiceTest {
//    @InjectMocks
//    private AuthService testAuthDAO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testCreateAuth_Positive() {
//        AuthData authData = new AuthData("authToken", "username");
//        assertDoesNotThrow(() -> testAuthDAO.createAuth(authData));
//        assertNotNull(testAuthDAO.getAuth("authToken"));
//    }
//
//    @Test
//    public void testCreateAuth_Negative() {
//        AuthData authData1 = new AuthData("authToken", "username1");
//        AuthData authData2 = new AuthData("authToken", "username2");
//        testAuthDAO.createAuth(authData1);
//        assertThrows(RuntimeException.class, () -> testAuthDAO.createAuth(authData2));
//    }
//
//    @Test
//    public void testGetAuth_Positive() {
//        AuthData authData = new AuthData("authToken", "username");
//        testAuthDAO.createAuth(authData);
//        AuthData result = testAuthDAO.getAuth("authToken");
//        assertNotNull(result);
//        assertEquals(authData, result);
//    }
//
//    @Test
//    public void testGetAuth_Negative() {
//        AuthData result = testAuthDAO.getAuth("invalidToken");
//        assertNull(result);
//    }
//
//    @Test
//    public void testDeleteAuth_Positive() {
//        AuthData authData = new AuthData("authToken", "username");
//        testAuthDAO.createAuth(authData);
//        assertDoesNotThrow(() -> testAuthDAO.deleteAuth("authToken"));
//        assertNull(testAuthDAO.getAuth("authToken"));
//    }
//
//    @Test
//    public void testDeleteAuth_Negative() {
//        assertDoesNotThrow(() -> testAuthDAO.deleteAuth("nonExistingToken"));
//    }
//}
