package com.inha.endgame.core.unitysocket;

import com.inha.endgame.core.io.ClientRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class UnitySocketWebSocketHandler extends TextWebSocketHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger("UnitySocketWebSocketHandler");

	private final UnitySocketService unitySocketService;
	private final SessionService sessionService;

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// 실행 중 에러가 발생하더라도 세션 연결이 죽지 않도록
		try {
			var messageString = message.getPayload();
			var request = this.unitySocketService.parseMessage(messageString);
			unitySocketService.publishRequest(session, request);
		} catch (Exception e) {
			unitySocketService.sendErrorMessage(session, e);
		}
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) {
		if(!sessionService.validateSession(session))
			throw new IllegalStateException("다른 세션에서 접속이 확인되어 종료합니다.");
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) {
		LOGGER.warn(exception.getMessage());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		LOGGER.warn("Connection Established.");

		sessionService.addSession(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		LOGGER.warn("Connection Closed. Status: " + status);

		sessionService.kickSession(session.getId());
	}
}
