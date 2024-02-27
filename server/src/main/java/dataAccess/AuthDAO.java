package dataAccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authRecord);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
