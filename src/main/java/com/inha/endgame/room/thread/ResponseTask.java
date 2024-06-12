package com.inha.endgame.room.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
public class ResponseTask implements Runnable {
    private final WebSocketSession session;
    private final String json;

    public ResponseTask(WebSocketSession session, String json) {
        this.session = session;
        this.json = json;
    }

    @Override
    public void run() {
        synchronized (session) {
            try {
                if (session != null && session.isOpen()) {
                    session.sendMessage(new TextMessage(json));
                }
            } catch (IOException e) {
                log.warn("전송 오류 : " + session.getId());
            }
        }
    }
}
