package dataAccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
//    public static void createConnection() throws Exception {
//        try (var conn = DriverManager.getConnection(connectionUrl, user, password)) {
//            try (var preparedStatement = conn.prepareStatement("SELECT 1+1")) {
//                var rs = preparedStatement.executeQuery();
//                rs.next();
//                System.out.println(rs.getInt(1));
//            }
//        }
//    }

    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    void storeUserPassword(String username, String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        // write the hashed password in database along with the user's other information
        writeHashedPasswordToDatabase(username, hashedPassword);
    }
    boolean verifyUser(String username, String providedClearTextPassword) {
        // read the previously hashed password from the database
        var hashedPassword = readHashedPasswordFromDatabase(username);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(providedClearTextPassword, hashedPassword);
    }

    private void writeHashedPasswordToDatabase(String username, String hashedPassword) {
        String query = "UPDATE users SET hashed_password = ? WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
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
