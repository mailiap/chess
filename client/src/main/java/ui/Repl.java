package ui;

import exception.ResponseException;
import static ui.EscapeSequences.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() throws ResponseException, IOException, URISyntaxException {
        System.out.println(SET_TEXT_COLOR_MAGENTA + " ♕ " + "Welcome to the chess game. Sign in to start.");
        System.out.println(RESET_TEXT_COLOR);
        System.out.print(client.help());

        Scanner scanner=new Scanner(System.in);
        var result="";
        var quit="";
        while (!quit.equals("quit")) {
            System.out.print("\n" + RESET_TEXT_COLOR + ">>> ");
            String line=scanner.nextLine();

            try {
                result = client.eval(line);
                quit = line;
                System.out.print(result);
            } catch (Throwable e) {
                var msg=e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }
}