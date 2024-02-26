package server;

import com.google.gson.Gson;
import spark.*;
import java.util.*;

public class Server {
    private ArrayList<String> names=new ArrayList<>();

    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clearApplication);
        Spark.post("/user", this::register);
//        Spark.post("/user", (req, res) -> (new register()).handleRequest(req, res));
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGames);
        Spark.put("/game", this::joinGame);
//        Spark.delete("/name/:name", this::deleteName);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private Object clearApplication(Request req, Response res) {
        names.remove(req.params(":name"));
        return clearApplication(req, res);
    }

    private Object register(Request req, Response res) {
        names.add(req.params(":name"));
        return register(req, res);
    }

    private Object login(Request req, Response res) {
        names.add(req.params(":name"));
        return login(req, res);
    }

    private Object logout(Request req, Response res) {
        names.remove(req.params(":name"));
        return logout(req, res);
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("name", names));
    }

    private Object createGames(Request req, Response res) {
        names.add(req.params(":name"));
        return createGames(req, res);
    }

    private Object joinGame(Request req, Response res) {
        names.add(req.params(":name"));
        return joinGame(req, res);
    }
}