package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {

    private ChessGame game;

    public LoadGame(ServerMessageType type, final ChessGame game) {
        super(type);
        this.game=game;
    }

    public ChessGame getGame() {
        return game;
    }
}
