package service;

import dataAccess.*;
import exception.ResponseException;
import model.*;
import java.util.*;

public class UserService {
    public static Object register(UserData user) throws ResponseException, DataAccessException {
        if ((user.username() == null) || user.password() == null || user.email() == null) {
            throw new ResponseException(400, "Error: bad request");
        }
        for (UserData existingUser : UserMemoryDAO.users.values()) {
            if (existingUser.email().equals(user.email()) || (UserMemoryDAO.users.containsKey(user.username()))) {
                throw new ResponseException(403, "Error: already taken");
            }
        }
        String authToken= UserMemoryDAO.generateAuthToken(user.username());
        AuthData authData=new AuthData(user.username(), authToken);
        UserMemoryDAO.users.put(user.username(), user);
        new AuthMemoryDAO().createAuth(authData);
        return authData;
    }
    public static Object login(UserData user) throws DataAccessException, ResponseException {
        UserData storedUser=UserMemoryDAO.users.get(user.username());
        if (storedUser == null || !storedUser.password().equals(user.password())) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        String authToken=UserMemoryDAO.generateAuthToken(user.username());
        AuthData authData=new AuthData(user.username(), authToken);
        AuthMemoryDAO.createAuth(authData);
        return authData;
    }
    public static void logout(String authToken) throws ResponseException {
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        boolean authTokenFound=false;
        for (AuthData authData : AuthMemoryDAO.authTokens.values()) {
            if (authData.authToken().equals(authToken)) {
                authTokenFound=true;
                AuthMemoryDAO.authTokens.remove(authData.authToken());
                break;
            }
        }
        if (!authTokenFound) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }
}