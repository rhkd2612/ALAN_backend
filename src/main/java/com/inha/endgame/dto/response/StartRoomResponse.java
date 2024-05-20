package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class StartRoomResponse implements ClientResponse {
    @Schema(description = "START_ROOM", defaultValue = "START_ROOM")
    private final ResponseType type;
    private final RoomState roomState;
    private Date nextStateAt;

    public StartRoomResponse(RoomState roomState, Date nextStateAt) {
        this.type = ResponseType.START_ROOM;
        this.roomState = roomState;
        this.nextStateAt = nextStateAt;
    }
}
