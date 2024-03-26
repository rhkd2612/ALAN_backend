package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StartRoomResponse implements ClientResponse {
    @Schema(description = "START_ROOM", defaultValue = "START_ROOM")
    private final ResponseType type;

    public StartRoomResponse() {
        this.type = ResponseType.START_ROOM;
    }
}
