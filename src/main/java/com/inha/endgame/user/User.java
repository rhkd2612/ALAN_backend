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

import com.inha.endgame.room.Room;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.Objects;
import java.util.UUID;

@Getter
public class User {
	private final String userId;
	private final String username;
	private String sessionId;
	//private final Room room;

	public User(String sessionId, String username, String userId) {
		this.sessionId = sessionId;
		this.username = username;
		this.userId = userId;
		//this.room = room;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public RoomUser toRoomUser() {
		return new RoomUser(this.userId, this.username, new Vector3D(0,0,0));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof User)) {
			return false;
		}

		return Objects.equals(username, ((User) obj).getUsername());
	}

	// This is naive implementation of hashCode() that just defers to username,
	// but will work for our purposes
	@Override
	public int hashCode() {
		return Objects.hashCode(username);
	}
}
