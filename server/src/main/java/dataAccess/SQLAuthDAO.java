package dataAccess;

import exception.ResponseException;
import model.*;
import java.sql.*;
import java.util.UUID;
import static dataAccess.DatabaseManager.getConnection;

public class SQLAuthDAO implements AuthDAO {

    public SQLAuthDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public void deleteAllAuthData() throws DataAccessException {
        try (Connection conn= getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE * FROM auths")) {
                int rowsAffected=preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String createAuthToken(String username) throws SQLException, DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES('" + username + "', '" + authToken + "')")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    return authToken;
                }
            }
        }
        return null;
    }

    public void deleteAuthToken(String authToken) throws DataAccessException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken = '" + authToken + "'")) {
                preparedStatement.setString(1, authToken);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserByAuthToken(String authToken) throws DataAccessException, SQLException {
        String username = null;
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken = '" + authToken + "'")) {
                preparedStatement.setString(1, authToken);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        }
        return username;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `type` ENUM('CAT', 'DOG', 'FISH', 'FROG', 'ROCK') DEFAULT 'CAT',
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(type),
              INDEX(name)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
