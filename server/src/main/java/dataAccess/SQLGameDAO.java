//package dataAccess;
//
//import chess.ChessGame;
//import exception.ResponseException;
//import model.AuthData;
//import model.GameData;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//
//import static dataAccess.DatabaseManager.getConnection;
//
//public class SQLGameDAO implements GameDAO {
//
//    public SQLGameDAO() throws ResponseException, DataAccessException {
//        configureDatabase();
//    }
//    public GameData getGame(int gameId) throws DataAccessException, SQLException {
//        var conn=getConnection();
//        int gameid=0;
//        String white = null;
//        String black = null;
//        String name = null;
//        ChessGame game = null;
//        try (var preparedStatement=conn.prepareStatement("SELECT * FROM game WHERE gameID=?")) {
//            preparedStatement.setInt(1, gameId);
//
//            try (var rs=preparedStatement.executeQuery()) {
//                while (rs.next()) {
//                    gameid=rs.getInt("gameID");
//                    white = rs.getString("whiteUsername");
//                    black = rs.getString("blackUsername");
//                    name = rs.getString("gameName");
//                    game =(ChessGame) rs.getObject("game");
//
//                    System.out.printf("gameID: %d, whiteUsername: %s, blackUsername: %s, gameName: %s, game: %s", gameid, white, black, name, game);
//                }
//            }
//        }
//        if (gameid != 0) {
//            return new GameData(gameid, white, black, name, game);
//        } else {
//            return null;
//        }
//    }
//
//    public void newGame(int gameId, GameData gameInfo) throws DataAccessException, SQLException {
////    void insertPet(String name) throws SQLException, DataAccessException {
//        var conn = getConnection();
//
//        var statement = "INSERT INTO game (gameID, gameName) VALUES('" + gameId + gameInfo.gameName() + "')";
//        System.out.println(statement);
//        try (var preparedStatement = conn.prepareStatement(statement)) {
//            preparedStatement.executeUpdate();
//        }
//    }
//
//    public void updateGame(int gameId, GameData updatedGame) throws DataAccessException, SQLException {
//        var conn=getConnection();
//
//        try (var preparedStatement=conn.prepareStatement("UPDATE game SET gameName=? WHERE gameID=?")) {
//            preparedStatement.setInt(1, gameId);
//            preparedStatement.setString(2, updatedGame.gameName());
//
//            preparedStatement.executeUpdate();
//        }
//    }
//
//    public int generateGameID() throws DataAccessException {
//        return 0;
//    }
//
//    public void deleteAllGameData() throws DataAccessException {
//        try (Connection conn= getConnection()) {
//            try (PreparedStatement preparedStatement=conn.prepareStatement("DELETE * FROM GameData")) {
//                int rowsAffected=preparedStatement.executeUpdate();
//                System.out.println(rowsAffected + " row(s) deleted successfully.");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS games (
//                id INT AUTO_INCREMENT PRIMARY KEY,
//                state JSON NOT NULL,
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
