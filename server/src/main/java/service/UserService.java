package service;

import dataAccess.*;
import exception.ResponseException;
import model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.*;

public class UserService {
    AuthDAO authMemory=new SQLAuthDAO();
    UserDAO userMemory=new SQLUserDAO();

    public UserService() throws ResponseException, DataAccessException, SQLException {
    }

    public Object register(UserData userInput) throws ResponseException {
        try {
            if ((userInput.username() == null) || userInput.password() == null || userInput.email() == null) {
                throw new ResponseException(400, "Error: bad request");
            }

            String existingUser=userMemory.checkExistingUser(userInput.username());
            if (existingUser != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            userMemory.createUser(userInput);
            String authToken=authMemory.createAuthToken(userInput.username());
            return new AuthData(userInput.username(), authToken);
        } catch (SQLException e) {
            throw new ResponseException(500, "Error: something went wrong");
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Error: something went wrong");
        }
    }

    public Object login(UserData userInput) throws ResponseException, SQLException, DataAccessException {
            UserData user=userMemory.getUserByUsername(userInput.username());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (user == null || !encoder.matches(userInput.password(), user.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String authToken=authMemory.createAuthToken(user.username());
            return new AuthData(user.username(), authToken);
        }

    public void logout(String authToken) throws ResponseException {
        boolean authTokenFound;
        try {
            if (authToken == null || authToken.isEmpty()) {
                throw new ResponseException(401, "Error: unauthorized");
            }
            authTokenFound=false;
                if (authMemory.getUserByAuthToken(authToken) != null) {
                    authTokenFound=true;
                    authMemory.deleteAuthToken(authToken);
                }
            if (!authTokenFound) {
                throw new ResponseException(401, "Error: unauthorized");
            }
//            authMemory.deleteAuthToken(authToken);
        } catch (SQLException e) {
            throw new ResponseException(500, e.getMessage());
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }
}