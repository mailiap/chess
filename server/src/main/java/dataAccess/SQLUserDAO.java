package dataAccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;

import static dataAccess.DatabaseManager.getConnection;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws SQLException, DataAccessException, ResponseException {
        configureDatabase();
    }

    public void deleteAllUserData() throws DataAccessException, SQLException {
        try (Connection conn= getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("TRUNCATE TABLE users")) {
                int rowsAffected=preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        }
    }

    public String checkExistingUser(String username) {
        String existingUser = null;
        try (Connection conn=getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT username FROM users WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    existingUser = rs.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return existingUser;
    }

    public void createUser(UserData userRecord) throws DataAccessException, SQLException {
        try (var conn=getConnection()) {
            var statement="INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement=conn.prepareStatement(statement)) {
                BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
                String hashedPassword=encoder.encode(userRecord.password());

                preparedStatement.setString(1, userRecord.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userRecord.email());
                preparedStatement.executeUpdate();
            }
        }
    }

    public UserData getUserByUsername(String username) throws DataAccessException, SQLException {
        UserData userData=null;
        try (Connection conn=getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet rs=preparedStatement.executeQuery();
                if (rs.next()) {
                    String password=rs.getString("password");
                    String email=rs.getString("email");

                    userData=new UserData(username, password, email);
                }
            }
        }
        return userData;
    }

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS users (
             username VARCHAR(255) NOT NULL,
             password VARCHAR(255) NOT NULL,
             email VARCHAR(255) NOT NULL,
             UNIQUE(username),
             UNIQUE(email)
     )
    """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn=getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement=conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
