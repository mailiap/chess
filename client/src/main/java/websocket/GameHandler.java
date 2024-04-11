package websocket;

import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;

public interface GameHandler {
    void updateGame(ChessGame game) throws ResponseException, DataAccessException;
    void printMessage(ServerMessage message);
}
