package dataAccess;

import model.UserData;

public interface UserDAO {
    static void createUser(UserData userRecord) throws DataAccessException {};
    static void getUser(String username) throws DataAccessException {};
    static void generateAuthToken(String username) throws DataAccessException {}
}
