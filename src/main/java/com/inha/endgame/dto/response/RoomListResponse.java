
package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.dto.RoomDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomListResponse implements ClientResponse {
    @Schema(description = "ROOM_LIST", defaultValue = "ROOM_LIST")
    private final ResponseType type;
    private final List<RoomDto> rooms;

    public RoomListResponse(List<RoomDto> rooms) {
        this.type = ResponseType.ROOM_LIST;
        this.rooms = rooms;
    }
}
