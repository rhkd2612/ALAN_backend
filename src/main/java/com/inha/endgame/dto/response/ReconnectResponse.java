package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.dto.ReconnectInfo;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReconnectResponse implements ClientResponse {
    @Schema(description = "RECONNECT", defaultValue = "RECONNECT")
    private final ResponseType type;
    private final RoomUser reconnectUser;
    private final ReconnectInfo reconnectInfo;

    public ReconnectResponse(RoomUser reconnectUser, ReconnectInfo reconnectInfo) {
        this.type = ResponseType.RECONNECT;
        this.reconnectUser = reconnectUser;
        this.reconnectInfo = reconnectInfo;
    }
}