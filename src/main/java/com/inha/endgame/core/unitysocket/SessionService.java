package com.inha.endgame.core.unitysocket;

import com.inha.endgame.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionService {
    private final Map<String, User> connectUser = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> connectSession = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Long> lastPingTime = new ConcurrentHashMap<>();

    private final static int PING_PONG_TIME_OUT = 10000;

    public SessionService() {
        startPingScheduler();
    }

    private void startPingScheduler() {
        Thread pingScheduler = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(500);
                    long currentTimestamp = new Date().getTime();
                    for(var entry : lastPingTime.entrySet()) {
                        var session = entry.getKey();
                        var lastPingTimeAt = entry.getValue();

                        if (!connectSession.containsKey(session.getId())) {
                            lastPingTime.remove(session);
                            continue;
                        }

                        if(currentTimestamp - lastPingTimeAt > PING_PONG_TIME_OUT) {
                            try {
                                session.close(CloseStatus.GOING_AWAY);
                            } catch (IOException e) {} finally {
                                lastPingTime.remove(session);
                            }
                        }
                    }
                } catch (InterruptedException e) {}
            }
        });
        pingScheduler.setDaemon(true);
        pingScheduler.start();
    }

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
        lastPingTime.put(session, new Date().getTime());
    }

    public void kickSession(String sessionId) {
        WebSocketSession session = connectSession.get(sessionId);
        if(session == null)
            return;

        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("세션 종료"));
                session.close();
            }
            connectSession.remove(sessionId);
            lastPingTime.remove(session);
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

    public void updatePingTime(WebSocketSession session) {
        lastPingTime.put(session, new Date().getTime());
    }
}
