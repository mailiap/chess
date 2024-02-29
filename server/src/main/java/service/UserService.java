package service;

import dataAccess.*;
import exception.ResponseException;
import model.*;
import java.util.*;

public class UserService implements UserDAO {
    public static Map<String, UserData> users = new HashMap<>();
    public static Map<String, AuthData> authTokens = new HashMap<>();

    public static UserData createUser(UserData userRecord) {
        String user = userRecord.username();
        if (users.containsKey(user)) {
            throw new RuntimeException("User '" + user + "' already exists");
        }
        return users.put(user, userRecord);
    }

    public static UserData getUser(String username) {
        return users.get(username);
    }

    public static Object register(UserData user) throws ResponseException {
        if (user.username().isEmpty() || user.password().isEmpty() || user.email().isEmpty()) {
            throw new ResponseException(400, "Error: bad request");
        }
        for (UserData existingUser : users.values()) {
            if (existingUser.email().equals(user.email()) || (users.containsKey(user.username()))) {
                throw new ResponseException(403, "Error: already taken");
            }
        }
        String authToken = generateAuthToken();
        AuthData authData = new AuthData(user.username(), authToken);
        users.put(user.username(), user);
        authTokens.put(authToken, authData);
        return authData;
    }

    public static Object login(UserData user) {
        UserData storedUser=users.get(user.username());
        if (storedUser != null && storedUser.password().equals(user.password())) {
            String authToken=generateAuthToken();
            AuthData authData=new AuthData(user.username(), authToken);
            authTokens.put(authToken, authData);
            return authData;
        } else {
            Map<String, Object> errorResponse=new HashMap<>();
            errorResponse.put("message", "Error: unauthorized");
            return errorResponse;
        }
    }

    public static void logout(String authToken) throws ResponseException {
        // Check if authToken is provided
        if (authToken == null || authToken.isEmpty()) {
            throw new ResponseException(401, "Error: unauthorized");
        }
        // Check if the provided authToken exists
        boolean authTokenFound = false;
        for (AuthData authData : authTokens.values()) {
            if (authData.authToken().equals(authToken)) {
                authTokenFound = true;
                authTokens.remove(authData.authToken());
                break;
            }
        }
        // If authToken is not found, throw Unauthorized error
        if (!authTokenFound) {
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    private static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}