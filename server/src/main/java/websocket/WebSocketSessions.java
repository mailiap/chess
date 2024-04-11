package websocket;

import org.eclipse.jetty.websocket.api.*;
import org.glassfish.tyrus.core.cluster.SessionListener;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    public final Map<Integer, Map<String, Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(int gameID, String authToken, Session session) {
        var sessionList = new HashMap<String, Session>();
        if (!sessionMap.isEmpty()) {
            sessionMap.get(gameID).put(authToken, session);
        } else {
//            sessionList.putAll(authToken, session);
            sessionList.put(authToken, session);
            sessionMap.put(gameID, sessionList);
        }
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        sessionMap.get(gameID).remove(authToken, session);
    }

    public void removeSession(Session session) {
        sessionMap.remove(session);
    }

    public List<Session> getSessionsForGame1(int gameID) {
        return (List<Session>) sessionMap.get(gameID);
    }

    public Map<String, Session> getSessionsForGame(int gameID) {
        if (sessionMap.containsKey(gameID)) {
            return sessionMap.get(gameID);
        } else {
            return Collections.emptyMap();
        }
    }
}

