package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public AuthData createAuth(AuthData authRecord) {
        String authToken = authRecord.authToken();
        if (authTokens.containsKey(authToken)) {
            throw new RuntimeException("Authentication token '" + authToken + "' already exists");
        }
        return authTokens.put(authToken, authRecord);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authTokens.remove(authToken);
    }
}
