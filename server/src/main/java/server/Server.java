package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.UserMemoryDAO;
import exception.ResponseException;
import model.*;
import org.eclipse.jetty.security.PropertyUserStore;
import org.eclipse.jetty.server.Authentication;
import service.*;
import spark.*;
import java.util.*;

public class Server {
    private ArrayList<String> games=new ArrayList<>();

//    public static void main(String[] args) {
//        new Server().run(8080);
//    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApplication);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGames);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private Object clearApplication(Request req, Response res) {
        try {
            AuthService.deleteAllAuth();
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object register(Request req, Response res) {
        try {
            String jsonString = req.body();
            Gson serializer = new Gson();
            UserData userDataRequest = serializer.fromJson(jsonString, UserData.class);

            Object result = UserService.register(userDataRequest);

            return serializer.toJson(result);
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object login(Request req, Response res) {
        try {
            String jsonString = req.body();
            Gson serializer = new Gson();
            LoginRequest userLoginRequest = serializer.fromJson(jsonString, LoginRequest.class);
            UserData userDataRequest = new UserData(userLoginRequest.username(), userLoginRequest.password(), null);

            Object result = UserService.login(userDataRequest);
            return serializer.toJson(result);

//            if (result instanceof AuthData) {
//                res.status(200);
//                return serializer.toJson(result);
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }
//
    private Object logout(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");

            UserService.logout(authToken);

            return "{}";
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }

    private Object listGames(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");

            List<GameData> games = GameService.listGames(authToken);

            String result = new Gson().toJson(Map.of("games", games));
            return result;
        } catch (ResponseException e) {
            // Handle any ResponseException (e.g., Unauthorized)
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }

    private Object createGames(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            String jsonString = req.body();
            Gson serializer = new Gson();
            GameData createGameRequest = serializer.fromJson(jsonString, GameData.class);

            Object result = GameService.createGame(createGameRequest, authToken);

            return serializer.toJson(result);
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object joinGame(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            String jsonString = req.body();
            Gson serializer = new Gson();
            JoinGameRequest joinGameRequest = serializer.fromJson(jsonString, JoinGameRequest.class);
            int gameID = joinGameRequest.gameID();
            String playerColor= joinGameRequest.playerColor();

            GameService.joinGame(gameID, playerColor, authToken);
            return "{}";
        } catch (ResponseException e) {
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            res.status(500);
            return "{\"message\": \"Internal Server Error\"}";
        }
    }
}