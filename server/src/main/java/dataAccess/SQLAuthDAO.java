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

    public void deleteAllAuthData() throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
                int rowsAffected = preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String createAuthToken(String username) throws SQLException, DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
                return authToken;
            }
        }
    }

    public void deleteAuthToken(String authToken) throws DataAccessException, SQLException {
        try (Connection conn= getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE FROM auths WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                int rowsAffected=preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        }
    }

    public String getUserByAuthToken(String authToken) throws DataAccessException, SQLException {
        String username = null;
        try (Connection conn=getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username, authToken FROM auths WHERE authToken = ?")) {
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
            id INT AUTO_INCREMENT PRIMARY KEY,
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                UNIQUE(authToken),
                UNIQUE(username)
            )
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
