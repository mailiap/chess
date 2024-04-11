package ui;

import chess.ChessGame;
import exception.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import websocket.GameHandler;

import static ui.EscapeSequences.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Repl implements GameHandler {
    private final ChessClient client;

    public Repl(String serverUrl) throws ResponseException {
        client = new ChessClient(serverUrl, this);
    }

    public void run() throws ResponseException, IOException, URISyntaxException {
        System.out.println(SET_TEXT_COLOR_MAGENTA + " ♕ " + "Welcome to the chess game. Sign in to start.");
        System.out.println(RESET_TEXT_COLOR);
        System.out.print(client.help());

        Scanner scanner=new Scanner(System.in);
        var result="";
        while (!result.contains("Exiting")) {
            System.out.print("\n" + RESET_TEXT_COLOR + ">>> ");
            String line=scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg=e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void updateGame(ChessGame game) {}

    public void printMessage(ServerMessage message) {
        System.out.println(SET_TEXT_COLOR_RED + message.toString());
    }
}