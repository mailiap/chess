package model;

import chess.ChessGame;

import java.util.Objects;

public class GameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final ChessGame game;

    GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

        this.gameID = gameID;
        this.whiteUsername= whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final GameData gameData=(GameData) o;
        return this.gameID == gameData.gameID && Objects.equals(this.whiteUsername, gameData.whiteUsername) && Objects.equals(this.blackUsername, gameData.blackUsername) && Objects.equals(this.gameName, gameData.gameName) && Objects.equals(this.game, gameData.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.gameID, this.whiteUsername, this.blackUsername, this.gameName, this.game);
    }

    @Override
    public String toString() {
        return "GamaData{" +
                "gameID=" + gameID +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'' +
                ", gameName='" + gameName + '\'' +
                ", game=" + game +
                '}';
    }
}
