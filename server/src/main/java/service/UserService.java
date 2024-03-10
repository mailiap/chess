package service;

import dataAccess.*;
import exception.ResponseException;
import model.*;

import java.sql.SQLException;
import java.util.*;

public class UserService {

    AuthMemoryDAO authMemory = new AuthMemoryDAO();
    UserMemoryDAO userMemory = new UserMemoryDAO();

    public Object register(UserData userInput) throws ResponseException, DataAccessException {
        if ((userInput.username() == null) || userInput.password() == null || userInput.email() == null) {
            throw new ResponseException(400, "Error: bad request");
        }

//            if (existingUser.email().equals(user.email())

            String existingUser=userMemory.checkExistingUser(userInput.username());
//            if (existingUser == null || userVal.email().equals(userInput.email())) {
        if (existingUser == null) {

            throw new ResponseException(403, "Error: already taken");
        }

        userMemory.createUser(userInput);
        String authToken = authMemory.createAuthToken(userInput.username());
        return new AuthData(userInput.username(), authToken);
    }
    public Object login(UserData userInput) throws DataAccessException, ResponseException {
        UserData user = userMemory.getUserByUsername(userInput.username());

        if (user == null || !user.password().equals(userInput.password())) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        String authToken = authMemory.createAuthToken(user.username());
        return new AuthData(user.username(), authToken);
    }

//    public static Object login(UserData user) throws DataAccessException, ResponseException {
//        UserData storedUser=UserMemoryDAO.users.get(user.username());
//
//        if (storedUser == null || !storedUser.password().equals(user.password())) {
//            throw new ResponseException(401, "Error: unauthorized");
//        }
//
//        String authToken=UserMemoryDAO.generateAuthToken(user.username());
//        AuthData authData=new AuthData(user.username(), authToken);
//        AuthMemoryDAO.createAuth(authData);
//        return authData;
//    }

    public void logout(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }

        authMemory.deleteAuthToken(authToken);
    }
}