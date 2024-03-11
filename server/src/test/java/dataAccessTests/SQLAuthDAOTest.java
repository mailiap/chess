package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class SQLAuthDAOTest {

    private SQLAuthDAO SQLAuth;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException, ResponseException {
        SQLAuth = new SQLAuthDAO();
    }

    @AfterEach
    public void tearDown() throws SQLException, DataAccessException {
        SQLAuth.deleteAllAuthData();
    }

    @Test
    public void testDeleteAllAuthData_Positive() throws DataAccessException, SQLException {
        String authToken1 = SQLAuth.createAuthToken("user1");
        String authToken2 = SQLAuth.createAuthToken("user2");

        SQLAuth.deleteAllAuthData();

        assertNull(SQLAuth.getUserByAuthToken(authToken1));
        assertNull(SQLAuth.getUserByAuthToken(authToken2));
    }

    @Test
    public void testDeleteAllAuthData_Negative() throws ResponseException, DataAccessException, SQLException {
        SQLAuthDAO SQLAuth = new SQLAuthDAO() {
            @Override
            public void deleteAllAuthData() throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, SQLAuth::deleteAllAuthData);
    }

    @Test
    public void testCreateAuthToken_Positive() throws DataAccessException, SQLException {
        String username = "testuser";

        String authToken = SQLAuth.createAuthToken(username);

        assertNotNull(authToken);
    }

    @Test
    public void testCreateAuthToken_Negative() throws ResponseException, DataAccessException {
        SQLAuthDAO SQLAuth = new SQLAuthDAO() {
            @Override
            public String createAuthToken(String username) throws SQLException, DataAccessException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, () -> SQLAuth.createAuthToken("testuser"));
    }

     @Test
    public void testDeleteAuthToken_Positive() throws DataAccessException, SQLException {
         String authToken1 = SQLAuth.createAuthToken("testuser1");
         String authToken2 = SQLAuth.createAuthToken("testuser2");

        SQLAuth.deleteAuthToken(authToken1);

        assertNull(SQLAuth.getUserByAuthToken(authToken1));
        assertNotNull(SQLAuth.getUserByAuthToken(authToken2));
    }

    @Test
    public void testDeleteAuthToken_Negative() throws ResponseException, DataAccessException {
        SQLAuthDAO SQLAuth = new SQLAuthDAO() {
            @Override
            public void deleteAuthToken(String authToken) throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, () -> SQLAuth.deleteAuthToken("testuser"));
    }

    @Test
    public void testGetUserByAuthToken_Positive() throws DataAccessException, SQLException {
        String username = "user1";
        String authToken = SQLAuth.createAuthToken(username);

        String retrievedUsername = SQLAuth.getUserByAuthToken(authToken);

        assertEquals(username, retrievedUsername);
    }

    @Test
    public void testGetUserByAuthToken_Negative() throws SQLException, DataAccessException {
        assertNull(SQLAuth.getUserByAuthToken("nonexistentToken"));
    }
}
