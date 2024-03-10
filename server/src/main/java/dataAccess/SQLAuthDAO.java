//package dataAccess;
//
//import exception.ResponseException;
//import model.*;
//
//import java.sql.*;
//
//import static dataAccess.DatabaseManager.getConnection;
//import static java.sql.Statement.RETURN_GENERATED_KEYS;
//import static java.sql.Types.NULL;
//
//public class SQLAuthDAO implements AuthDAO {
//
//    public SQLAuthDAO() throws ResponseException, DataAccessException {
//        configureDatabase();
//    }
//
//    public void createAuth(AuthData authRecord) throws SQLException, DataAccessException {
//        var conn = getConnection();
//
//        var statement = "INSERT INTO auth (username, authtoken) VALUES('" + authRecord.username() + authRecord.authToken() + "')";
//        System.out.println(statement);
//        try (var preparedStatement = conn.prepareStatement(statement)) {
//            preparedStatement.executeUpdate();
//        }
//    }
//
//    public AuthData getAuth(String authToken) throws DataAccessException, SQLException {
//        var conn = getConnection();
//        String username = null;
//        String authtoken = null;
//        try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authtoken=?")) {
//            preparedStatement.setString(1, authToken);
//            try (var rs = preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    username = rs.getString("username");
//                    authtoken = rs.getString("authtoken");
//
//                    System.out.printf("username: %s, authtoken: %s%n", username, authtoken);
//                }
//            }
//        }
//        if (username != null && authtoken != null) {
//            return new AuthData(username, authtoken);
//        } else {
//            return null;
//        }
//    }
//
//    public void deleteAllAuthData() throws DataAccessException {
//        try (Connection conn= getConnection()) {
//            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE * FROM AuthData")) {
//                int rowsAffected=preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS auths (
//              `id` int NOT NULL AUTO_INCREMENT,
//              `name` varchar(256) NOT NULL,
//              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
//              `json` TEXT DEFAULT NULL,
//              PRIMARY KEY (`id`),
//              INDEX(type),
//              INDEX(name)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
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
