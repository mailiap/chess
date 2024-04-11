package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {

    private int gameID;
    private ChessMove move;

    public MakeMove(String authToken, final int gameID, final ChessMove move) {
        super(authToken);
        this.gameID=gameID;
        this.move=move;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
