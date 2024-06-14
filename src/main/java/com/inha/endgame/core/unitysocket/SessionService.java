package com.inha.endgame.core.unitysocket;

import com.inha.endgame.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, User> connectUser = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> connectSession = new ConcurrentHashMap<>();

    public User findUserBySessionId(String sessionId) {
        return connectUser.get(sessionId);
    }

    public WebSocketSession findSessionBySessionId(String sessionId) {
        return connectSession.get(sessionId);
    }

    public void addUser(String sessionId, User user) {
        connectUser.put(sessionId, user);
    }

    public void addSession(WebSocketSession session) {
        connectSession.put(session.getId(), session);
    }

    public void kickSession(String sessionId) {
        WebSocketSession session = connectSession.get(sessionId);
        try {
            if (session != null) {
                if(session.isOpen()) {
                    session.sendMessage(new TextMessage("세션 종료"));
                    session.close();
                }

                connectSession.remove(sessionId);
            }
        } catch (Exception e) {
            throw new IllegalStateException("존재하지 않는 세션 정보입니다.");
        }
    }

    public boolean validatePrevSessionId(String prevSessionId) {
        User user = connectUser.get(prevSessionId);
        return user != null;
    }

    public void changeSession(String prevSessionId, WebSocketSession nextSession) {
        User user = connectUser.get(prevSessionId);
        if(user != null) {
            this.kickSession(prevSessionId);
            user.setSessionId(nextSession.getId());
        } else
            throw new IllegalStateException("유저 validation error");

        this.addUser(nextSession.getId(), user);
    }

    public boolean validateSession(WebSocketSession session) {
        return connectUser.containsKey(session.getId());
    }
}
