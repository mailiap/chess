package service;

import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthData register(UserData user) {
        // Check if the username already exists
        UserData existingUser = userDAO.getUser(user.username());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }
        // Register the new user
        userDAO.createUser(user);
        // Generate authentication data and return it
        return login(user);
    }

    public AuthData login(UserData user) {
        // Check if the user exists
        UserData existingUser = userDAO.getUser(user.username());
        if (existingUser == null || !existingUser.password().equals(user.password())) {
            throw new RuntimeException("Invalid username or password");
        }
        // Generate and return authentication data
        return new AuthData(generateAuthToken(user.username()), user.username());
    }

    public void logout(UserData user) {
        // No action needed for logout in this basic example
    }

    private String generateAuthToken(String username) {
        // In a real-world scenario, this method should generate a secure token
        return username + "_token";
    }
}