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

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AddUserResponse implements ClientResponse {
    @Schema(description = "ADD_USER", defaultValue = "ADD_USER")
    private final ResponseType type;
    private final String userId;
    private final List<RoomUser> roomUsers = new ArrayList<>();

    public AddUserResponse(String userId, List<RoomUser> roomUsers) {
        this.type = ResponseType.ADD_USER;
        this.userId = userId;
        this.roomUsers.addAll(roomUsers);
    }
}
