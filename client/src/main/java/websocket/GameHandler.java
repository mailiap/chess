package websocket;

import chess.ChessGame;
import dataAccess.DataAccessException;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;

import java.sql.SQLException;

public interface GameHandler {
    void updateGame(ChessGame game) throws ResponseException, DataAccessException;
    void printMessage(ServerMessage serverType, String message) throws ResponseException, DataAccessException;
}
