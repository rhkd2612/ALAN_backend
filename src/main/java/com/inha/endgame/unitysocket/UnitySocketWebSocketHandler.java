/*
 * Copyright 2017 L0G1C (David B) - logiclodge.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inha.endgame.unitysocket;

import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.exception.ExceptionMessageTranslator;
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
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// 실행 중 에러가 발생하더라도 세션 연결이 죽지 않도록
		try {
			String messageString = message.getPayload();
			this.unitySocketService.parseMessage(session, messageString);
		} catch (Exception e) {
			this.unitySocketService.sendErrorMessage(session, e);
		}
	}

	@Override
	protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		if(!sessionService.validateSession(session))
			throw new IllegalStateException("다른 세션에서 접속이 확인되어 종료합니다.");
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		LOGGER.warn(exception.getMessage());
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		LOGGER.warn("Connection Established.");
		LOGGER.warn("User: " + session.getId());
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		LOGGER.warn("Connection Closed. Status: " + status);
		LOGGER.warn("User: " + session.getId());
	}
}
