package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {

    private int gameID;

    public Resign(String authToken, final int gameID) {
        super(authToken);
        this.gameID=gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
