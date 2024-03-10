package chess;

import com.google.gson.Gson;

public class ChessGameSerializer {
    private Gson gson;

    public ChessGameSerializer() {
        this.gson = new Gson();
    }

    // Serialize ChessGame to JSON string
    public String serializeChessGame(ChessGame game) {
        return gson.toJson(game);
    }

    // Deserialize JSON string to ChessGame
    public ChessGame deserializeChessGame(String json) {
        return gson.fromJson(json, ChessGame.class);
    }
}
