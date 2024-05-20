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


    public void addSession(WebSocketSession session, User user) {
        connectUser.put(session.getId(), user);
        connectSession.put(session.getId(), session);
    }

    public void kickSession(String sessionId) {
        WebSocketSession session = connectSession.get(sessionId);
        try {
            if (session != null && session.isOpen()) {
                session.sendMessage(new TextMessage("다른 클라이언트의 접속으로 인해 연결이 끊어졌습니다."));
                session.close();
            }
        } catch (Exception e) {
            throw new IllegalStateException("세션 종료 실패");
        }

        connectSession.remove(sessionId);
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

        this.addSession(nextSession, user);
    }

    public boolean validateSession(WebSocketSession session) {
        return connectUser.containsKey(session.getId());
    }
}
