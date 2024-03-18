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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inha.endgame.core.ClientEvent;
import com.inha.endgame.core.ClientRequest;
import com.inha.endgame.core.ClientResponse;
import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.exception.ExceptionMessageTranslator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitySocketService {

	private static final Logger LOGGER = LoggerFactory.getLogger("UnitySocketService");

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ApplicationEventPublisher publisher;
	private final ExceptionMessageTranslator exceptionMessageTranslator;

	public void parseMessage(WebSocketSession session, String messageString) throws IOException {
		ClientRequest cr = objectMapper.readValue(messageString, ClientRequest.class);
		publisher.publishEvent(new ClientEvent<>(cr, session));
	}

	public void sendMessage(WebSocketSession session, ClientResponse clientResponse) throws IOException {
		String json = objectMapper.writeValueAsString(clientResponse);
		session.sendMessage(new TextMessage(json));
	}

	public void sendMessageAll(List<WebSocketSession> sessions, ClientResponse clientResponse) throws IOException {
		String json = objectMapper.writeValueAsString(clientResponse);

		sessions.forEach(session -> {
			try {
				session.sendMessage(new TextMessage(json));
			} catch (IOException e) {
				LOGGER.warn("전송 오류 : " + session.getId());
			}
		});
	}

	public void sendErrorMessage(WebSocketSession session, Exception e) {
		try {
			String errMessage = exceptionMessageTranslator.translate(e);
			this.sendMessage(session, new ErrorResponse(errMessage));
		} catch (IOException ex) {
			LOGGER.warn("error 생성 실패");
		}
	}
}
