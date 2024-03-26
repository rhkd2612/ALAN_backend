package com.inha.endgame.user;

import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.core.unitysocket.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class UserService {
	private final Map<String, User> mapUser = new ConcurrentHashMap<>(); // FIXME 필요에 따라 DB로 이관
	private final SessionService sessionService;

	public User getUser(String username) {
		return mapUser.get(username);
	}

	public Collection<User> getAllUser() {
		return Collections.unmodifiableCollection(mapUser.values());
	}

	public synchronized User addUser(WebSocketSession session, AddUserRequest request) {
		var sessionId = session.getId();
		var username = request.getUsername();
		var nickname=  request.getNickname();

		if(mapUser.containsKey(username)) {
			var prevUser = mapUser.get(username);
			if(!prevUser.getSessionId().equals(sessionId))
				sessionService.changeSession(prevUser.getSessionId(), session);
			return prevUser;
		}

		var newUser = new User(sessionId, username, nickname);
		mapUser.put(username, newUser);

		sessionService.addSession(session, newUser);
		return newUser;
	}

	public void syncRoom(User user) {
		user.syncRoom();
	}

	public void logout(User user) {
		mapUser.remove(user.getUsername());
		sessionService.kickSession(user.getSessionId());
	}
}

