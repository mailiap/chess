package dataAccess;

import exception.ResponseException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public interface AuthDAO {
     void deleteAllAuthData() throws DataAccessException;
     String createAuthToken(String username) throws DataAccessException, SQLException;
     void deleteAuthToken(String authToken) throws DataAccessException, ResponseException;
     String getUserByAuthToken(String authToken) throws DataAccessException, SQLException;
}
