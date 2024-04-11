package webSocketMessages.serverMessages;

public class Error extends ServerMessage {

    private String errorMessage;

    public Error(ServerMessageType type, final String errorMessage) {
        super(type);
        this.errorMessage=errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
