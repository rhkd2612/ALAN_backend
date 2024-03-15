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

package com.inha.endgame.dto.response;

import com.inha.endgame.core.ClientResponse;
import com.inha.endgame.core.ResponseType;
import com.inha.endgame.user.User;

import java.util.Collection;
import java.util.HashSet;

public class FriendsListResponse extends ClientResponse {
	private Collection<String> usernames = new HashSet<>();

	public FriendsListResponse(Collection<User> users) {
		setType(ResponseType.FRIENDSLIST);
		for (User user : users) {
			usernames.add(user.getUsername());
		}

	}

	public Collection<String> getUsernames() {
		return usernames;
	}

	public void setUsernames(Collection<String> usernames) {
		this.usernames = usernames;
	}
}
