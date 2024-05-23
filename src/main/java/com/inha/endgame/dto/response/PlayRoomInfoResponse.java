
package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class PlayRoomInfoResponse implements ClientResponse {
    @Schema(description = "PLAY_ROOM_INFO", defaultValue = "PLAY_ROOM_INFO")
    private final ResponseType type;
    private final List<RoomUser> roomUsers = new ArrayList<>();
    private final Date roomEndAt;
    private final Date boomAt;

    public PlayRoomInfoResponse(List<RoomUser> roomUsers, Date roomEndAt, Date boomAt) {
        this.type = ResponseType.PLAY_ROOM_INFO;
        this.roomUsers.addAll(roomUsers);
        this.roomEndAt = roomEndAt;
        this.boomAt = boomAt;
    }
}
