package service;

import dataAccess.*;
import exception.ResponseException;

import java.sql.SQLException;

public class AuthService {

    AuthDAO authMemory = new SQLAuthDAO();
    UserDAO userMemory = new SQLUserDAO();
    GameDAO gameMemory = new SQLGameDAO();

    public AuthService() throws ResponseException, DataAccessException, SQLException {
    }

    public void clearDatabase() throws ResponseException {
        try {
            userMemory.deleteAllUserData(); //clear users
            gameMemory.deleteAllGameData(); //clear games
            authMemory.deleteAllAuthData(); // clear auths
        } catch (SQLException e) {
            throw new ResponseException(500, "Error: something went wrong");
        } catch(DataAccessException e) {
            throw new ResponseException(500, "Error: something went wrong");
        }
    }
}
