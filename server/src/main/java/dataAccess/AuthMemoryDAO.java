package dataAccess;

import exception.ResponseException;
import model.AuthData;
import service.GameService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthMemoryDAO implements AuthDAO {
    public static Map<String, AuthData> authData=new HashMap<>();

    public void deleteAllAuthData() {
        authData.clear();
    }

    public String createAuthToken(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuthTokenData = new AuthData(username, authToken);

        authData.put(authToken, newAuthTokenData);
        return authToken;
    }

    public void deleteAuthToken(String authToken) throws ResponseException {
        boolean authTokenFound=false;
        for (AuthData auth : authData.values()) {
            if (authToken.equals(auth.authToken())) {
                authTokenFound=true;
                authData.remove(authToken);
                break;
            }
        }

        if (!authTokenFound) {
            throw new ResponseException(401, "Error: unauthorized");
        }

    }

    public String getUserByAuthToken(String authToken) {
        if (authData.containsKey(authToken)) {
            AuthData username =authData.get(authToken);
            return username.username();
        }
        return null;
    }
}

