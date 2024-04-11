package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    
    private String message;

    public Notification(ServerMessageType type, final String message) {
        super(type);
        this.message=message;
    }

    public String getMessage() {
        return message;
    }
}
