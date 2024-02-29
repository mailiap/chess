package service;

import dataAccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

import static service.GameService.games;
import static service.UserService.users;

public class AuthService implements AuthDAO {
    private static Map<String, AuthData> authTokens = new HashMap<>();

    public AuthData createAuth(AuthData authRecord) {
        String authToken = authRecord.authToken();
        if (authTokens.containsKey(authToken)) {
            throw new RuntimeException("Authentication token '" + authToken + "' already exists");
        }
        return authTokens.put(authToken, authRecord);
    }

    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    public static void deleteAllAuth() {
        users.clear();
        games.clear();
        authTokens.clear();
    }
}
