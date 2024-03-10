package service;

import dataAccess.*;

public class AuthService {

    AuthMemoryDAO authMemory = new AuthMemoryDAO();
    UserMemoryDAO userMemory = new UserMemoryDAO();
    GameMemoryDAO gameMemory = new GameMemoryDAO();



    public void clearDatabase() {
        userMemory.deleteAllUserData(); //clear users
        gameMemory.deleteAllGameData(); //clear games
        authMemory.deleteAllAuthData(); // clear auths
    }
}
