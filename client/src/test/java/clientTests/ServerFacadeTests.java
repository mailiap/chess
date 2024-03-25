package clientTests;

import dataAccess.DataAccessException;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.*;
import server.*;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @BeforeEach
    public void tearDown() throws ResponseException {
        facade.clearApplication();
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    public void testClearApplication_Positive() {
        assertDoesNotThrow(() -> facade.clearApplication());
    }

    @Test
    public void testClearApplication_Negative() {
        server.stop(); // Stop server to simulate network error
        assertThrows(ResponseException.class, () -> facade.clearApplication());
        server.run(8080); // Restart server after test
    }

    @Test
    void testRegister_Positive() {
        assertDoesNotThrow(() -> {
            facade.register(new UserData("player1", "password", "p1@email.com"));
        });
    }

    @Test
    public void testRegister_Negative() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));

        assertThrows(ResponseException.class, () -> {
            facade.register(new UserData("player1", "password1", "player1@mail.com"));
        });
    }

    @Test
    public void testLogin_Positive() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));

        assertDoesNotThrow(() -> {
            facade.login(new UserData("player1", "password", "p1@email.com"));
        });
    }

    @Test
    public void testLogin_Negative() {
        assertThrows(ResponseException.class, () -> {
            facade.login(new UserData("player1", "password", null));
        });
    }

    @Test
    public void testCreateGame_Positive() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login(new UserData("player1", "password", null));

        assertDoesNotThrow(() -> {
            facade.createGame(new GameData(0, null, null, "game1", null));
        });
    }

    @Test
    public void testCreateGame_Negative() {
        assertThrows(ResponseException.class, () -> {
            facade.createGame(new GameData(0, null, null, null, null));
        });
    }

    @Test
    public void testListGames_Positive() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login(new UserData("player1", "password", null));
        facade.createGame(new GameData(0, null, null, "game1", null));
        facade.createGame(new GameData(0, null, null, "game2", null));

        assertDoesNotThrow(() -> {
            facade.listGames();
        });
    }

    @Test
    public void testListGames_Negative() {
        assertThrows(ResponseException.class, () -> {
            facade.listGames();
        });
    }

    @Test
    public void testJoinGame_Positive() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login(new UserData("player1", "password", null));
        facade.createGame(new GameData(0, null, null, "game1", null));

        assertDoesNotThrow(() -> {
            facade.joinGame(1, "WHITE");
        });
    }

    @Test
    public void testJoinGame_Negative() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login(new UserData("player1", "password", null));
        facade.createGame(new GameData(0, null, null, "game1", null));
        facade.joinGame(1, "WHITE");

        assertThrows(ResponseException.class, () -> {
            facade.joinGame(1, "WHITE");
        });
    }

    @Test
    public void testLogout_Positive() throws ResponseException {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login(new UserData("player1", "password", null));

        assertDoesNotThrow(() -> {
            facade.logout();
        });
    }

    @Test
    public void testLogout_Negative() {
        assertThrows(ResponseException.class, () -> {
            facade.logout();
        });
    }


}