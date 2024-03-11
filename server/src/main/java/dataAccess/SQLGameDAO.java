package dataAccess;

import chess.ChessGame;
import exception.*;
import model.*;
import java.sql.*;
import java.util.*;

import static dataAccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public void deleteAllGameData() throws DataAccessException {
        try (Connection conn= getConnection()) {
            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE * FROM games")) {
                int rowsAffected=preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) deleted successfully.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<GameData> getGames() throws DataAccessException, SQLException {
        Collection<GameData> gameDataList = new ArrayList<>();
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM games")) {
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    int gameId = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = new ChessGame();
                    gameDataList.add(new GameData(gameId, whiteUsername, blackUsername, gameName, game));
                }
            }
        }
        return gameDataList;
    }


    public int newGame(String username, String gameName) throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO GameData (username, gameName) VALUES(?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, gameName);
                return preparedStatement.executeUpdate();
            }
        }
    }

    public GameData getGameByID(int gameId) throws DataAccessException {
        GameData gameData = null;
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM GameData WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    // Assuming you have a method to create a ChessGame object based on game ID
                    ChessGame game = new ChessGame();
                    gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, game);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameData;
    }

    public void updatePlayerColor(int gameID, String username, String playerColor) throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE games SET " +
                    (playerColor.equalsIgnoreCase("WHITE") ? "whiteUsername" : "blackUsername") + " = ? WHERE gameID = ?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " row(s) updated successfully.");
            }
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
                id INT AUTO_INCREMENT PRIMARY KEY,
                state JSON NOT NULL,
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
