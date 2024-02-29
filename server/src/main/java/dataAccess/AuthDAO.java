package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authRecord);
    AuthData getAuth(String authToken);
    static void deleteAuth(String authToken) {}
    static void deleteAllAuth() {}
}
