package dataAccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    AuthData register(UserData user);
    AuthData login(UserData user);
    void logout(UserData user);
    String generateAuthToken(String username);
    void createUser(UserData userRecord);
    UserData getUser(String username);
}
