package service;
import static dataAccess.GameMemoryDAO.games;
import static dataAccess.UserMemoryDAO.users;
import static dataAccess.AuthMemoryDAO.authTokens;

public class AuthService {
    public static void deleteAllAuth() {
        users.clear(); //clear users
        games.clear(); //clear games
        authTokens.clear(); //clear tokens
    }
}
