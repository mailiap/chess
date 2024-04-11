import chess.*;
import server.Server;
import spark.Spark;
import websocket.WebSocketHandler;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server runServer = new Server();
        runServer.run(8080);
    }
}