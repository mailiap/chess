package dataAccess;

import model.GameData;
import java.util.List;

public interface GameDAO {
    GameData createGame(GameData gameRecord);
    GameData getGame(int gameId);
    List<GameData> listGames();
    void updateGame(int gameId, GameData updatedGame);
}
