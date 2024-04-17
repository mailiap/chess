package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

    private ChessGame game;
    private ChessGame.TeamColor playerColor;

    public LoadGame(ServerMessageType type, final ChessGame game, final ChessGame.TeamColor playerColor) {
        super(type);
        this.game=game;
        this.playerColor=playerColor;
    }

//    public LoadGame(ServerMessageType type, final ChessGame game) {
//        super(type);
//        this.game=game;
//        this.playerColor=ChessGame.TeamColor.WHITE;
//    }

    public ChessGame getGame() {
        return game;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
