package dataAccess;

import model.AuthData;

public interface AuthDAO {
     static void createAuth(AuthData authRecord) throws DataAccessException {}
     static void getAuth(String authToken) throws DataAccessException {}
}
