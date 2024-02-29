package exception;

public class ResponseException extends Exception {
    static private int statusCode;
    public ResponseException(int statusCode, String message) {
        super(message);
        this.statusCode=statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}