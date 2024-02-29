package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
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
        Spark.exception(ResponseException.class, this::exceptionHandler);


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.getStatusCode());
    }

    private Object clearApplication(Request req, Response res) {
        try {
            AuthService.deleteAllAuth(); // Call the method to clear the entire database
            return "{}";
        } catch (Exception e) {
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object register(Request req, Response res) {
        try {
            // Parse JSON request body into UserData object
            String jsonString = req.body();
            Gson serializer = new Gson();
            UserData userDataRequest = serializer.fromJson(jsonString, UserData.class);

            // Call register method to register the new user
            Object result = UserService.register(userDataRequest);

            // Serialize the result to JSON and return it
            return serializer.toJson(result);
        } catch (ResponseException e) {
            // Handle any ResponseException (e.g., Bad Request, Username/Email already taken)
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object login(Request req, Response res) {
        try {
            String jsonString = req.body();
            Gson serializer = new Gson();
            LoginRequest userLoginRequest = serializer.fromJson(jsonString, LoginRequest.class);
            // Convert LoginRequest to UserData
            UserData userDataRequest = new UserData(userLoginRequest.username(), userLoginRequest.password(), null);
            // Call login method
            Object result = UserService.login(userDataRequest);
            // If login successful, return authToken
            if (result instanceof AuthData) {
                res.status(200);
                return serializer.toJson(result);
            } else {
                // If login failed, return error message
                res.status(401);
                return serializer.toJson(result);
            }
        } catch (Exception e) {
            // Handle any unexpected exceptions
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }

    private Object logout(Request req, Response res) {
        try {
            // Get authToken from the request header
            String authToken = req.headers("Authorization");
            // Call logout method with authToken
            UserService.logout(authToken);
            // Return empty JSON object
            return "{}";
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

    private Object listGames(Request req, Response res) {
        try {
            // Get authToken from the request header
            String authToken = req.headers("Authorization");

            // Retrieve list of games using the provided authToken
            List<GameData> games = GameService.listGames(authToken);

            // Serialize the list of games to JSON and return it
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
            // Parse JSON request body into CreateGameRequest object
            String jsonString = req.body();
            Gson serializer = new Gson();
            GameData createGameRequest = serializer.fromJson(jsonString, GameData.class);

            // Call createGame method to create the new game
            String authToken = req.headers("Authorization");
            Object result = GameService.createGame(createGameRequest, authToken);

            // Serialize the result to JSON and return it
            return serializer.toJson(result);
        } catch (ResponseException e) {
            // Handle any ResponseException (e.g., Bad Request, Unauthorized)
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }


    private Object joinGame(Request req, Response res) {
        try {
            // Extract required parameters from request
            String jsonString = req.body();
            Gson serializer = new Gson();
            JoinGameRequest joinGameRequest = serializer.fromJson(jsonString, JoinGameRequest.class);
            int gameID = joinGameRequest.gameID();
            String playerColor = joinGameRequest.playColor();
            String authToken = req.headers("Authorization");

            // Call joinGame method with the provided parameters
            Object result = GameService.joinGame(gameID, playerColor, authToken);

            // Serialize the result to JSON and return it
            return serializer.toJson(result);
        } catch (ResponseException e) {
            // Handle any ResponseException (e.g., Bad Request, Unauthorized, Player color already taken)
            res.status(e.getStatusCode());
            return "{\"message\": \"" + e.getMessage() + "\"}";
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            res.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }
}

// take the requet pull out all the important info (put into an object)
// put it in the format the srervice is requesting (maybe GSOM)
// run service on that object (body)
// the thing the service returns convert to Gson and then retrun that

//pet shop example
