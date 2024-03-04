package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

//import static service.GameService.games;

public class AuthMemoryDAO implements AuthDAO {
    public static Map<String, AuthData> authTokens = new HashMap<>();

    public static void createAuth(AuthData authRecord) throws DataAccessException {
        String authToken = authRecord.authToken();
        if (authTokens.containsKey(authToken)) {
            throw new DataAccessException("Authentication token '" + authToken + "' already exists");
        }
         authTokens.put(authToken, authRecord);
    }

    public static AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }
}
