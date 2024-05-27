package com.inha.endgame.user;

import com.inha.endgame.core.unitysocket.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {
	private final Map<Long, Map<String, User>> mapUser = new ConcurrentHashMap<>();
	private final SessionService sessionService;

	public User getUser(long roomId, String username) {
		return mapUser.get(roomId).get(username);
	}

	public Collection<User> getAllUser(long roomId) {
		if(mapUser.get(roomId) == null)
			return Collections.emptyList();

		return Collections.unmodifiableCollection(mapUser.get(roomId).values());
	}

	public synchronized User addUser(WebSocketSession session, long roomId, String username, String nickname) {
		if(!mapUser.containsKey(roomId))
			mapUser.put(roomId, new ConcurrentHashMap<>());

		var sessionId = session.getId();
		if(mapUser.get(roomId).containsKey(username))
			throw new IllegalArgumentException("이미 있는 유저입니다.");

		var newUser = new User(sessionId, username, nickname);
		mapUser.get(roomId).put(username, newUser);

		sessionService.addSession(session, newUser);
		return newUser;
	}

	public synchronized void reconnect(WebSocketSession session, long roomId, String username) {
		var sessionId = session.getId();

		if(mapUser.get(roomId).containsKey(username)) {
			var prevUser = mapUser.get(roomId).get(username);
			var prevUserSessionId = prevUser.getSessionId();

			if(!prevUserSessionId.equals(sessionId) && sessionService.validatePrevSessionId(prevUserSessionId)) {
				sessionService.changeSession(prevUserSessionId, session);
				return;
			}
		}

		throw new IllegalArgumentException("재접속할 유저 정보가 없습니다.");
	}

	public String leaveRoom(long roomId, String leaveUsername) {
		User user = mapUser.get(roomId).get(leaveUsername);
		if(user == null)
			return null;

		String sessionId = new String(user.getSessionId());
		mapUser.get(roomId).remove(leaveUsername);

		return sessionId;
	}

	public void syncRoom(User user) {
		user.syncRoom();
	}

	public void logout(long roomId, User user) {
		mapUser.get(roomId).remove(user.getUsername());
		sessionService.kickSession(user.getSessionId());
	}
}

