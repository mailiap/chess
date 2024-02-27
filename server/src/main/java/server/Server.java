package server;

import com.google.gson.Gson;
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
        Spark.delete("/db", (req, res) -> clearApplication(req, res));
        Spark.post("/user", (req, res) -> register(req, res));
        Spark.post("/session", (req, res) -> login(req, res));
        Spark.delete("/session", (req, res) -> logout(req, res));
        Spark.get("/game", (req, res) -> listGames(req, res));
        Spark.post("/game", (req, res) -> createGames(req, res));
        Spark.put("/game", (req, res) -> joinGame(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private Object clearApplication(Request req, Response res) {
        games.remove(req.params(":game"));
        return listGames(req, res);
    }

    private Object register(Request req, Response res) {
        games.add(req.params(":game"));
        return listGames(req, res);
    }
    private Object login(spark.Request req, spark.Response res) {
        games.add(req.params(":game"));
        return listGames(req, res);
    }

    private Object logout(Request req, Response res) {
        games.remove(req.params(":game"));
        return listGames(req, res);
    }

    private Object listGames(Request req, Response res) {
        res.type("application/json");
        return new Gson().toJson(Map.of("game", games));
    }

    private Object createGames(Request req, Response res) {
        games.add(req.params(":game"));
        return listGames(req, res);
    }

    private Object joinGame(Request req, Response res) {
        games.add(req.params(":game"));
        return listGames(req, res);
    }
}
