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
        return null;
    }

    public void createUser(UserData userRecord) throws DataAccessException, SQLException {
        var conn=getConnection();
        try {
            var statement="INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (var preparedStatement=conn.prepareStatement(statement)) {
                preparedStatement.setString(1, userRecord.username());
                preparedStatement.setString(2, userRecord.password());
                preparedStatement.setString(3, userRecord.email());
                preparedStatement.executeUpdate();

                storeUserPassword(conn, userRecord.username(), userRecord.password());
            }
        } finally {
            conn.close();
        }
    }

    public UserData getUserByUsername(String username) throws DataAccessException, SQLException {
        UserData userData=null;
        try (Connection conn=getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?")) {
                preparedStatement.setString(1, username);
                ResultSet rs=preparedStatement.executeQuery();
                if (rs.next()) {
                    String hashedPassword=rs.getString("password");
                    String email=rs.getString("email");

                    if (verifyUser(username, hashedPassword)) {
                        userData=new UserData(username, hashedPassword, email);
                    }
                }
            }
        }
    return userData;
}

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS users (
         id INT AUTO_INCREMENT PRIMARY KEY,
             username VARCHAR(255) NOT NULL,
             password VARCHAR(255) NOT NULL,
             hashed_password VARCHAR(255) NOT NULL,
             email VARCHAR(255) NOT NULL,
             UNIQUE(username),
             UNIQUE(email)
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

    void storeUserPassword(Connection conn, String username, String password) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(conn, username, hashedPassword);
    }

    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }

    private void writeHashedPasswordToDatabase(Connection conn, String username, String hashedPassword) throws DataAccessException {
//        var conn = getConnection();
        String statment = "UPDATE users SET hashed_password = ? WHERE username = ?";

        try (var preparedStatement=conn.prepareStatement(statment)) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String readHashedPasswordFromDatabase(String username) {
        String query = "SELECT hashed_password FROM users WHERE username = ?";
        String hashedPassword = null;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                hashedPassword = rs.getString("hashed_password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return hashedPassword;
    }
}
