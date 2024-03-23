package server;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void clearApplication() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData userInput) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, userInput, AuthData.class);
    }

    public AuthData login(UserData userInput) throws ResponseException {
        var path = "/session";
        AuthData authData = this.makeRequest("POST", path, userInput, AuthData.class);
        authToken = authData.authToken();
        return authData;
    }

    public void createGame(GameData gameInput) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, gameInput, GameData.class);
    }

    public GameData[] listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameData[].class);
    }

    public void joinGame(int gameID, String playerColor) throws ResponseException {
        String path = "/game";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("gameID", String.valueOf(gameID));
        requestBody.put("playerColor", playerColor);
        this.makeRequest("PUT", path, requestBody, String.class);
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
        authToken = null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
//            http.setReadTimeout(50000000);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("Authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Authorization", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response=null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody=http.getInputStream()) {
                InputStreamReader reader=new InputStreamReader(respBody);
                if (responseClass != null) {
                    response=new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var statusMessage = http.getResponseMessage();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, status + " Error: " + statusMessage);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}