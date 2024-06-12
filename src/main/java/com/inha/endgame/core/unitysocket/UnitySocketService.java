package com.inha.endgame.core.unitysocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inha.endgame.core.async.ResponseQueue;
import com.inha.endgame.core.async.ResponseTask;
import com.inha.endgame.core.exception.ExceptionMessageTranslator;
import com.inha.endgame.core.io.*;
import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UnitySocketService {
	private static final Logger LOGGER = LoggerFactory.getLogger("UnitySocketService");

	private static int networkDelay = 0;
	private static int networkBounce = 0;

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ApplicationEventPublisher publisher;
	private final ExceptionMessageTranslator exceptionMessageTranslator;
	private final RoomService roomService;
	private final UserService userService;
	private final SessionService sessionService;
	private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

	private final ResponseQueue responseQueue;

	private final static long LATENCY_TIME = 100;

	public static int getNetworkDelay() {
		if(UnitySocketService.networkDelay == 0)
			return 0;

		return Math.max(0, UnitySocketService.networkDelay + (-UnitySocketService.networkBounce + RandomUtils.nextInt(0, UnitySocketService.networkBounce * 2)));
	}

	public static void setNetworkDelay(int networkDelay, int networkBounce) {
		UnitySocketService.networkDelay = networkDelay;
		UnitySocketService.networkBounce = networkBounce;
	}

	public void parseMessage(WebSocketSession session, String messageString) throws IOException {
		try { Thread.sleep(getNetworkDelay()); } catch (Exception e){}

		ClientRequest cr = objectMapper.readValue(messageString, ClientRequest.class);

		// 딜레이 넷코드 처리
		if(cr instanceof RoomDelayRequest) {
			RoomDelayRequest delayRequest = (RoomDelayRequest) cr;
			Date now = new Date();
			if(delayRequest.getRequestAt() != null) {
				Date responseAt = delayRequest.getResponseAt(); // 패킷이 도착해야하는 시간

				// 반드시 보내야하는 패킷이 아닌데 늦게 도착한 경우 무시
				if (now.after(responseAt) && !delayRequest.isNeedSuccess())
					return;

				// 늦게 도착한 경우 보정 로직 추가
				long latency = now.getTime() - delayRequest.getResponseAt().getTime();
				delayRequest.setResponseAt(new Date(responseAt.getTime() + latency + LATENCY_TIME));
			}
		}

		publisher.publishEvent(new ClientEvent<>(cr, session));
	}

	private void checkDelayResponse(ClientResponse clientResponse, Date responseAt) {
		if(responseAt == null)
			return;

		if(clientResponse instanceof RoomDelayResponse) {
			RoomDelayResponse delayResponse = (RoomDelayResponse) clientResponse;
			delayResponse.setResponseAt(responseAt);
		}
	}

	public synchronized void sendMessage(String sessionId, ClientResponse clientResponse) throws IOException {
		sendMessage(sessionId, clientResponse, null);
	}

	public synchronized void sendMessage(String sessionId, ClientResponse clientResponse, Date responseAt) throws IOException {
		try { Thread.sleep(getNetworkDelay()); } catch (Exception e){}

		checkDelayResponse(clientResponse, responseAt);

		WebSocketSession session = sessionService.findSessionBySessionId(sessionId);

		if(session.isOpen()) {
			String json = objectMapper.writeValueAsString(clientResponse);
			responseQueue.submitTask(new ResponseTask(session, json));
		}
	}

	public synchronized void sendMessage(WebSocketSession session, ClientResponse clientResponse) throws IOException {
		sendMessage(session, clientResponse, null);
	}

	public synchronized void sendMessage(WebSocketSession session, ClientResponse clientResponse, Date responseAt) throws IOException {
		try { Thread.sleep(getNetworkDelay()); } catch (Exception e){}

		if(session.isOpen()) {
			checkDelayResponse(clientResponse, responseAt);

			String json = objectMapper.writeValueAsString(clientResponse);
			responseQueue.submitTask(new ResponseTask(session, json));
		}
	}

	public synchronized void sendMessageRoom(long roomId, ClientResponse clientResponse) throws IOException {
		sendMessageRoom(roomId, clientResponse, null);
	}

	public synchronized void sendMessageRoom(long roomId, ClientResponse clientResponse, Date responseAt) throws IOException {
		try { Thread.sleep(getNetworkDelay()); } catch (Exception e){}

		checkDelayResponse(clientResponse, responseAt);

		var json = objectMapper.writeValueAsString(clientResponse);
		var room = roomService.findRoomById(roomId);
		if(room == null)
			throw new IllegalArgumentException("존재하지 않는 방에 대한 요청");

		room.getRoomUsers().keySet().forEach(username -> {
			var user = userService.getUser(roomId, username);
			if(user == null)
				return;

			var session = sessionService.findSessionBySessionId(user.getSessionId());
			responseQueue.submitTask(new ResponseTask(session, json));
		});
	}

	public void sendErrorMessage(WebSocketSession session, Exception e) {
		try {
			String errMessage = exceptionMessageTranslator.translate(e);
			LOGGER.error(errMessage);
			this.sendMessage(session, new ErrorResponse(errMessage));
		} catch (IOException ex) {
			LOGGER.warn("error 생성 실패");
		}
	}
}
