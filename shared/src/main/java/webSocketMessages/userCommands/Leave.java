package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {

    private int gameID;

    public Leave(String authToken, final int gameID) {
        super(authToken);
        this.gameID=gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }
}
