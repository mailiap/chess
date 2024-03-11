package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {

    private SQLUserDAO SQLUser;

    @BeforeEach
    public void setUp() throws DataAccessException, SQLException, ResponseException {
        SQLUser = new SQLUserDAO();
    }

    @AfterEach
    public void tearDown() throws SQLException, DataAccessException {
        SQLUser.deleteAllUserData();
    }

    @Test
    public void testDeleteAllUserData_Positive() throws DataAccessException, SQLException {
        SQLUser.createUser(new UserData("user1", "password1", "user1@example.com"));
        SQLUser.createUser(new UserData("user2", "password2", "user2@example.com"));

        SQLUser.deleteAllUserData();

        assertNull(SQLUser.getUserByUsername("user1"));
        assertNull(SQLUser.getUserByUsername("user2"));
    }

    @Test
    public void testDeleteAllUserData_Negative() throws ResponseException, DataAccessException, SQLException {
        SQLUserDAO SQLUser = new SQLUserDAO() {
            @Override
            public void deleteAllUserData() throws DataAccessException, SQLException {
                throw new SQLException("Connection failed");
            }
        };

        assertThrows(SQLException.class, SQLUser::deleteAllUserData);
    }

    @Test
    public void testCreateUser_Positive() throws DataAccessException, SQLException {
        UserData userData = new UserData("testuser", "password", "test@example.com");

        SQLUser.createUser(userData);

        assertEquals(userData, SQLUser.getUserByUsername("testuser"));
    }

    @Test
    public void testCreateUser_Negative() throws DataAccessException, SQLException {
        UserData userData = new UserData("testuser", "password", "test@example.com");

        SQLUser.createUser(userData);

        assertThrows(SQLException.class, () -> SQLUser.createUser(userData));
    }

    @Test
    public void testGetUserByUsername_Positive() throws DataAccessException, SQLException {
        UserData userData = new UserData("user1", "password1", "user1@example.com");
        SQLUser.createUser(userData);

        UserData retrievedUserData = SQLUser.getUserByUsername("user1");

        assertNotNull(retrievedUserData);
        assertEquals("user1", retrievedUserData.username());
        assertEquals("password1", retrievedUserData.password());
        assertEquals("user1@example.com", retrievedUserData.email());
    }

    @Test
    public void testGetUserByUsername_Negative() throws DataAccessException, SQLException {
        UserData userData = new UserData("user1", "password1", "user1@example.com");
        SQLUser.createUser(userData);

        UserData retrievedUserData = SQLUser.getUserByUsername("nonexistentuser");

        assertNull(retrievedUserData);
    }
}
