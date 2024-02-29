package dataAccess;

import model.GameData;

public interface GameDAO {
    static void createGame(GameData gameRecord) {}
    GameData getGame(int gameId);
    static void listGames() {}
    void updateGame(int gameId, GameData updatedGame);
    static void joinGame(GameData userDataRequest, String playerColor, String authToken) {}
}
