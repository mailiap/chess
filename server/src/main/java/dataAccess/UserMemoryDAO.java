package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserMemoryDAO implements UserDAO {
    public static Map<String, UserData> users=new HashMap<>();

    public static UserData getUser(String username) {
        return users.get(username);
    }

    public static String generateAuthToken(String username) {
        return UUID.randomUUID().toString();
    }
}
