package service;

import dataAccess.AuthDAO;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public AuthData createAuth(AuthData authRecord) {
        return authDAO.createAuth(authRecord);
    }

    public AuthData getAuth(String authToken) {
        return authDAO.getAuth(authToken);
    }

    public void deleteAuth(String authToken) {
        authDAO.deleteAuth(authToken);
    }
}
