package dataAccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;

import java.sql.SQLException;

public interface UserDAO {
    void deleteAllUserData() throws DataAccessException, SQLException;
    String checkExistingUser(String username) throws DataAccessException;
    void createUser(UserData userRecord) throws DataAccessException, SQLException;
    UserData getUserByUsername(String username) throws DataAccessException, SQLException;
}
