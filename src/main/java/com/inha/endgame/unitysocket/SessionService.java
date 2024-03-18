package com.inha.endgame.unitysocket;

import com.inha.endgame.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, User> connectUser = new ConcurrentHashMap<>();

    public void addSession(WebSocketSession session, User user) {
        connectUser.put(session.getId(), user);
    }

    public void findUserBySession(WebSocketSession session) {
        connectUser.get(session.getId());
    }

    public void kickSession(String sessionId) {
        connectUser.remove(sessionId);
    }

    public boolean validateSession(WebSocketSession session) {
        return connectUser.containsKey(session.getId());
    }
}
