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

package com.inha.endgame.core.unitysocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.core.exception.ExceptionMessageTranslator;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UnitySocketService {

	private static final Logger LOGGER = LoggerFactory.getLogger("UnitySocketService");

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ApplicationEventPublisher publisher;
	private final ExceptionMessageTranslator exceptionMessageTranslator;
	private final RoomService roomService;
	private final UserService userService;
	private final SessionService sessionService;

	public void parseMessage(WebSocketSession session, String messageString) throws IOException {
		ClientRequest cr = objectMapper.readValue(messageString, ClientRequest.class);
		publisher.publishEvent(new ClientEvent<>(cr, session));
	}

	public void sendMessage(WebSocketSession session, ClientResponse clientResponse) throws IOException {
		String json = objectMapper.writeValueAsString(clientResponse);
		session.sendMessage(new TextMessage(json));
	}

	public void sendMessageRoom(long roomId, ClientResponse clientResponse) throws IOException {
		var json = objectMapper.writeValueAsString(clientResponse);
		var room = roomService.findRoomById(roomId);
		if(room == null)
			throw new IllegalArgumentException("존재하지 않는 방에 대한 요청");

		room.getRoomUsers().keySet().forEach(userId -> {
			var user = userService.getUser(userId);
			if(user == null)
				return;

			var session = sessionService.findSessionBySessionId(user.getSessionId());
			if(session == null)
				return;

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
