package dataAccess;

import model.GameData;
import java.util.List;

public interface GameDAO {
    void createGame(GameData gameRecord);
    GameData getGame(int gameId);
    List<GameData> listGames();
    void updateGame(int gameId, GameData updatedGame);
}
