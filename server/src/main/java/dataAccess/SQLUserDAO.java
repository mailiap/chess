//package dataAccess;
//
//import exception.ResponseException;
//import model.AuthData;
//import model.UserData;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import static dataAccess.DatabaseManager.getConnection;
//
//public class SQLUserDAO implements UserDAO {
//
//    public SQLUserDAO() throws ResponseException, DataAccessException {
//        configureDatabase();
//    }
//
//    public void deleteAllUserData() throws DataAccessException, SQLException {
//        try (Connection conn= getConnection()) {
//            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE * FROM UserData")) {
//                int rowsAffected=preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
//            }
//        }
//    }
//
//    public void createUser(UserData userRecord) throws DataAccessException, SQLException {
//        var conn = getConnection();
//
//        var statement = "INSERT INTO users (username, password, email) VALUES('" + userRecord.username() + userRecord.password() + userRecord.email() + "')";
//        System.out.println(statement);
//        try (var preparedStatement = conn.prepareStatement(statement)) {
//            preparedStatement.executeUpdate();
//        }
//    }
//
//    public String getUser(String username) throws DataAccessException, SQLException {
//            String foundUsername = null;
//            try (Connection conn = DatabaseManager.getConnection()) {
//                try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM users WHERE username = ?")) {
//                    preparedStatement.setString(1, username);
//                    ResultSet rs = preparedStatement.executeQuery();
//                    if (rs.next()) {
//                        foundUsername = rs.getString("username");
//                    }
//                }
//            }
//            return foundUsername;
//        }
//
//    public String generateAuthToken(String username) throws DataAccessException {
//        return null;
//    }
//
//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS users (
//                 id INT AUTO_INCREMENT PRIMARY KEY,
//                 username VARCHAR(255) NOT NULL,
//                 hashed_password VARCHAR(255) NOT NULL,
//                 UNIQUE(username)
//             ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """
//    };
//
//    private void configureDatabase() throws ResponseException, DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (SQLException ex) {
//            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }
//}
