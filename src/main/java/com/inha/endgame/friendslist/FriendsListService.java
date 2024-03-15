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

package com.inha.endgame.friendslist;

import com.inha.endgame.core.ClientEvent;
import com.inha.endgame.dto.request.AddFriendRequest;
import com.inha.endgame.dto.request.FriendsListRequest;
import com.inha.endgame.dto.request.TestRequest;
import com.inha.endgame.dto.response.FriendsListResponse;
import com.inha.endgame.dto.response.KemonomimiResponse;
import com.inha.endgame.dto.response.TestResponse;
import com.inha.endgame.unitysocket.UnitySocketService;
import com.inha.endgame.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Service
public class FriendsListService {

	private static final Logger LOGGER = LoggerFactory.getLogger("FriendsListService");

	@Autowired
	private UnitySocketService unitySocketService;

	@Autowired
	private UserService userService;

	@EventListener
	public void onFriendsListRequest(ClientEvent<FriendsListRequest> event) {
		LOGGER.warn("Got Friends List Request");

		WebSocketSession session = event.getSession();
		try {
			unitySocketService.sendMessage(session, getFriendsListResponse(session));
			LOGGER.warn("Sending extra dummy response.  Client should be able to deal with it.");
			unitySocketService.sendMessage(session, new KemonomimiResponse());
		} catch (IOException ex) {
			LOGGER.error("Something Bad Happened", ex);
		}
	}

	@EventListener
	public void onAddFriendRequest(ClientEvent<AddFriendRequest> event) {
		LOGGER.warn("Got Add Friend Request");
		try {
			event.getSession().sendMessage(new TextMessage("Got Add Friend Request"));
		} catch (IOException ex) {
			LOGGER.error("Something Bad Happened", ex);
		}
	}

	@EventListener
	public void onTestRequest(ClientEvent<TestRequest> event) {
		LOGGER.warn("Test");
		try {
			TestRequest request = event.getClientRequest();
			Integer answer = request.getA() + request.getB() + request.getC();
			unitySocketService.sendMessage(event.getSession(), new TestResponse(answer));
		} catch (IOException e) {
			LOGGER.error("Something Bad Happened", e);
		}
	}

	private FriendsListResponse getFriendsListResponse(WebSocketSession session) {
		return new FriendsListResponse(userService.getFriendsList(userService.getUserFromSession(session)));
	}
}
