package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import exception.*;
import model.*;
import java.sql.*;
import java.util.*;

import static dataAccess.DatabaseManager.getConnection;

public class SQLGameDAO implements GameDAO {
    private Gson gson;


    public SQLGameDAO() throws ResponseException, DataAccessException {
        configureDatabase();
        gson = new Gson();
    }

    public void deleteAllGameData() throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
                int rowsAffected = preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
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
//                    ChessGame game = rs.getObject("game", ChessGame.class);
                    gameDataList.add(new GameData(gameId, whiteUsername, blackUsername, gameName, null));
                }
            }
        }
        return gameDataList;
    }


    public int newGame(String gameName) throws DataAccessException, SQLException {
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO games (gameName, game) VALUES(?, ?)",Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameName);
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                ChessGame chessGame = new ChessGame();
                chessGame.setBoard(board);
                chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
                preparedStatement.setString(2, new Gson().toJson(chessGame));
                preparedStatement.executeUpdate();

                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        }
    }

    public GameData getGameByID(int gameId) throws DataAccessException, SQLException {
        GameData gameData = null;
        try (Connection conn = getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * from games WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    String game = rs.getString("game");
                    ChessGame chessGame = new Gson().fromJson(game, ChessGame.class);
                    gameData = new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameData;
    }


    public void updatePlayerColor(int gameID, String username, String playerColor) throws DataAccessException, SQLException {
        var conn = getConnection();
        try {
            String statement;
            if (playerColor != null && playerColor.equals("WHITE")) {
                statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
            } else if (playerColor != null && playerColor.equals("BLACK")) {
                statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
            } else {
                statement = "UPDATE games SET whiteUsername = ?, blackUsername = ? WHERE gameID = ?";
            }
            try (var preparedStatement = conn.prepareStatement(statement)) {
                if (playerColor.equals("WHITE") || playerColor.equals("BLACK")) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setInt(2, gameID);
                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateGame(ChessGame game, int gameID) throws DataAccessException, SQLException {
        var conn = getConnection();
        try {
            var statement = "UPDATE games SET game = ? WHERE gameID = ?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, new Gson().toJson(game));
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS games (
        gameID INT AUTO_INCREMENT PRIMARY KEY,
           whiteUsername VARCHAR(255),
           blackUsername VARCHAR(255),
           gameName VARCHAR(255),
           game JSON
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