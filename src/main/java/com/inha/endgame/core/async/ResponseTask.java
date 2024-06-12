package com.inha.endgame.core.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
public class ResponseTask implements Runnable {
    private final WebSocketSession session;
    private final String json;
    private final boolean isResend;

    public ResponseTask(WebSocketSession session, String json) {
        this.session = session;
        this.json = json;
        this.isResend = false;
    }

    public ResponseTask(WebSocketSession session, String json, boolean isResend) {
        this.session = session;
        this.json = json;
        this.isResend = isResend;
    }

    public boolean isResend() {
        return isResend;
    }

    @Override
    public void run() {
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(json));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}