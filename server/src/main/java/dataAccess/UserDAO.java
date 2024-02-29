package dataAccess;

import model.UserData;

public interface UserDAO {
    static void createUser(UserData userRecord) {}
    static void getUser(String username) {}
    static void register(UserData user) {}
    static void login(UserData user) {}
    static void logout(UserData user) {}
    static void generateAuthToken(String username) {}
}
