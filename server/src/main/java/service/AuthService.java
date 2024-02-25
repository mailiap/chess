package service;

import dataAccess.AuthDAO;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData getAuth(String authToken) {
        // Retrieve authentication data using the provided token
        return authDAO.getAuth(authToken);
    }

    public void deleteAuth(String authToken) {
        // Delete authentication data associated with the provided token
        authDAO.deleteAuth(authToken);
    }
}
