package dataAccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData register(UserData user) {
        users.put(user.username(), user);
        String authToken = generateAuthToken(user.username());
        AuthData authData = new AuthData(authToken, user.username());
        authTokens.put(authToken, authData);
        return authData;
    }

    @Override
    public AuthData login(UserData user) {
        UserData storedUser = users.get(user.username());
        if (storedUser != null && storedUser.password().equals(user.password())) {
            String authToken = generateAuthToken(user.username());
            AuthData authData = new AuthData(authToken, user.username());
            authTokens.put(authToken, authData);
            return authData;
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    @Override
    public void logout(UserData user) {
        authTokens.values().removeIf(authData -> authData.username().equals(user.username()));
    }

    @Override
    public String generateAuthToken(String username) {
        return "token_" + username;
    }

    @Override
    public void createUser(UserData userRecord) {
        users.put(userRecord.username(), userRecord);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}