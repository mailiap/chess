import chess.*;

import server.Server;
import exception.ResponseException;
import ui.GameplayUI;
import ui.Repl;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {
    public static void main(String[] args) throws ResponseException, URISyntaxException, IOException {
        var piece=new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

//        Server runServer = new Server();
//        runServer.run(8080);

        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        
        System.out.println();
        new Repl(serverUrl).run();
    }
}