package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class SettingRoomResponse implements ClientResponse {
    @Schema(description = "SETTING_ROOM", defaultValue = "SETTING_ROOM")
    private final ResponseType type;
    private final List<RoomUser> roomNpcs = new ArrayList<>();;

    public SettingRoomResponse(List<RoomUser> roomUsers) {
        this.type = ResponseType.SETTING_ROOM;
        this.roomNpcs.addAll(roomUsers);
    }
}
