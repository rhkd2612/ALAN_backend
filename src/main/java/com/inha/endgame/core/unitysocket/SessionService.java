package com.inha.endgame.core.unitysocket;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RoomDelayRequest;
import com.inha.endgame.user.User;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class SessionService {
    private final Map<String, User> connectUser = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> connectSession = new ConcurrentHashMap<>();
    private final Map<WebSocketSession, Long> lastPingTime = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, ConcurrentHashMap<String, AtomicBoolean>> requestLock = new ConcurrentHashMap<>(); // 특정 유저의 다수 요청을 순서대로 이행하기 위해? 사용

    private final static int PING_PONG_TIME_OUT = 60000;

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

    public boolean requestLock(String sessionId, ClientRequest request) {
        var type = request.getType();
        var userLock = requestLock.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());
        var lock = userLock.computeIfAbsent(type.name(), k -> new AtomicBoolean(false));

        // 시간이 크게 소요되지 않으므로 스핀락 처리
        while(!lock.compareAndSet(false, true)) {
            // 중복 체크 요청인 경우 무시
            if(type.checkDuplicate())
                return false;
        }

        return true;
    }

    public void releaseLock(String sessionId, String type) {
        var userLock = requestLock.get(sessionId);

        AtomicBoolean lock = userLock.get(type);
        if(lock != null)
            lock.set(false);
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
