
package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CreateRoomResponse implements ClientResponse {
    @Schema(description = "CREATE_ROOM", defaultValue = "CREATE_ROOM")
    private final ResponseType type;
    private final long roomId;

    public CreateRoomResponse(long roomId) {
        this.type = ResponseType.CREATE_ROOM;
        this.roomId = roomId;
    }
}
