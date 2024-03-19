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

package com.inha.endgame.user;

import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.unitysocket.SessionService;
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

	public User getUser(String userId) {
		return mapUser.get(userId);
	}

	public Collection<User> getAllUser() {
		return Collections.unmodifiableCollection(mapUser.values());
	}

	public synchronized User addUser(WebSocketSession session, AddUserRequest request) throws NoSuchAlgorithmException {
		if(sessionService.validateSession(session))
			throw new IllegalArgumentException("이미 입장한 유저입니다.");

		var sessionId = session.getId();
		var userName = request.getUserName();
		var userId = createIdByUserName(userName);

		if(mapUser.containsKey(userId)) {
			var prevUser = mapUser.get(userId);
			sessionService.changeSession(prevUser.getSessionId(), session);
			return prevUser;
		}

		// TODO 회원정보 계정명 뿐만 아니라 닉네임으로 추가
		var newUser = new User(sessionId, userName,  userId);
		mapUser.put(userId, newUser);

		sessionService.addSession(session, newUser);
		return newUser;
	}

	public void enterRoom(User user) {
		user.enterRoom();
	}

	public void logout(User user) {
		mapUser.remove(user.getUserId());
		sessionService.kickSession(user.getSessionId());
	}

	private static String createIdByUserName(String name) throws NoSuchAlgorithmException {
		var digest = MessageDigest.getInstance("SHA-256");
		var hashBytes = digest.digest(name.getBytes());
		var hashNumber = new BigInteger(1, hashBytes);
		return hashNumber.toString().substring(0, 12);
	}
}

