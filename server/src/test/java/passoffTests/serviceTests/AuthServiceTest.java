package passoffTests.serviceTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.AuthService;
import dataAccess.AuthDAO;
import model.AuthData;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {
    @Mock
    private AuthDAO authDAO;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAuth_Positive() {
        AuthData expectedAuthData = new AuthData("example_auth", "example_username");
        when(authDAO.getAuth("validToken")).thenReturn(expectedAuthData);
        AuthData result = authService.getAuth("validToken");
        assertNotNull(result);
        assertEquals(expectedAuthData, result);
    }

    @Test
    public void testGetAuth_Negative_InvalidToken() {
        when(authDAO.getAuth("invalidToken")).thenReturn(null);
        assertThrows(RuntimeException.class, () -> authService.getAuth("invalidToken"));
    }

    @Test
    public void testDeleteAuth_Positive() {
        assertDoesNotThrow(() -> authService.deleteAuth("validToken"));
        verify(authDAO, times(1)).deleteAuth("validToken");
    }
}
