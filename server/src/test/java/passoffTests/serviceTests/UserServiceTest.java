package passoffTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.UserService;
import dataAccess.UserDAO;
import model.UserData;
import model.AuthData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_Positive() {
        UserData userData = new UserData("testUser", "password123", "test@example.com");
        AuthData expectedAuthData = new AuthData("example_auth", "testUser");
        when(userDAO.register(userData)).thenReturn(expectedAuthData);
        AuthData result = userService.register(userData);
        assertNotNull(result);
        assertEquals(expectedAuthData, result);
    }

    @Test
    public void testRegister_Negative_ExistingUser() {
        UserData userData = new UserData("existingUser", "password123", "existing@example.com");
        when(userDAO.register(userData)).thenThrow(new RuntimeException("User already exists"));
        assertThrows(RuntimeException.class, () -> userService.register(userData));
    }

    @Test
    public void testLogin_Positive() {
        UserData userData = new UserData("testUser", "password123", "test@example.com");
        AuthData expectedAuthData = new AuthData("example_auth", "testUser");
        when(userDAO.login(userData)).thenReturn(expectedAuthData);
        AuthData result = userService.login(userData);
        assertNotNull(result);
        assertEquals(expectedAuthData, result);
    }

    @Test
    public void testLogin_Negative_InvalidCredentials() {
        UserData userData = new UserData("testUser", "wrongPassword", "test@example.com");
        when(userDAO.login(userData)).thenThrow(new RuntimeException("Invalid credentials"));
        assertThrows(RuntimeException.class, () -> userService.login(userData));
    }

    @Test
    public void testLogout_Positive() {
        UserData userData = new UserData("testUser", "password123", "test@example.com");
        assertDoesNotThrow(() -> userService.logout(userData));
    }
}
