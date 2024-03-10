package dataAccess;

import model.UserData;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserMemoryDAO implements UserDAO {
    public static Map<String, UserData> userData=new HashMap<>();

    public void deleteAllUserData() {
        userData.clear();
    }

    public String checkExistingUser(String username) {
        if (!userData.containsKey(username)) {
            return username;
        } else {
            return null;
        }
    }

    public void createUser(UserData userRecord) {
        userData.put(userRecord.username(), userRecord);
    }

    public UserData getUserByUsername(String username) {
        return userData.get(username);
    }
}
