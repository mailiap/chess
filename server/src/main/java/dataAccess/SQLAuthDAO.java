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
            try (PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)")) {
                insertStatement.setString(1, authToken);
                insertStatement.setString(2, username);
                insertStatement.executeUpdate();
            }
        }
        return authToken;
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
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        }
        return username;
    }

    public AuthData getAuthByAuthToken(String inputAuthToken) throws DataAccessException, SQLException {
        AuthData authData = null;
        try (Connection conn=getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM auths WHERE authToken = ?")) {
                preparedStatement.setString(1, inputAuthToken);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    String authToken = rs.getString("authToken");
                    authData = new AuthData(username, authToken);
                }
            }
        }
        return authData;
    }

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS auths (
                authToken VARCHAR(255) NOT NULL PRIMARY KEY,
                username VARCHAR(255) NOT NULL,
                UNIQUE(authToken)
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
