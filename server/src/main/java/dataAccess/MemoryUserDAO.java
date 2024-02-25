package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData userRecord) {
        users.put(userRecord.username(), userRecord);
    }

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }
}
