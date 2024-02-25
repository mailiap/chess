package dataAccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData userRecord);
    UserData getUser(String username);
}
