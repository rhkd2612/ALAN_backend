
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
    private final String nickname;
    private final List<RoomUser> roomUsers = new ArrayList<>();

    public AddUserResponse(String nickname, List<RoomUser> roomUsers) {
        this.type = ResponseType.ADD_USER;
        this.nickname = nickname;
        this.roomUsers.addAll(roomUsers);
    }
}
