package dataAccess;

import model.AuthData;
import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    // Simulate in-memory storage for user data
    private final Map<String, UserData> users = new HashMap<>();
    // Simulate in-memory storage for authentication data
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData register(UserData user) {
        // Simulate registering a user by adding them to the users map
        users.put(user.username(), user);
        // Generate auth token and associate it with the username
        String authToken = generateAuthToken(user.username());
        AuthData authData = new AuthData(authToken, user.username());
        authTokens.put(authToken, authData);
        return authData;
    }

    @Override
    public AuthData login(UserData user) {
        // Check if user exists and credentials match
        UserData storedUser = users.get(user.username());
        if (storedUser != null && storedUser.password().equals(user.password())) {
            // Generate auth token and associate it with the username
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
        // Remove authentication data associated with the user
        authTokens.values().removeIf(authData -> authData.username().equals(user.username()));
    }

    @Override
    public String generateAuthToken(String username) {
        // Simple method to generate an auth token (can be replaced with more secure methods)
        return "token_" + username;
    }

    @Override
    public void createUser(UserData userRecord) {
        // Simulate creating a user by adding them to the users map
        users.put(userRecord.username(), userRecord);
    }

    @Override
    public UserData getUser(String username) {
        // Retrieve user data by username
        return users.get(username);
    }
}
