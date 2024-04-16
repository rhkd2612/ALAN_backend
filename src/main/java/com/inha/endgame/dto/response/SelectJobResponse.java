package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUserType;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class SelectJobResponse implements ClientResponse {
    @Schema(description = "SELECT_JOB", defaultValue = "SELECT_JOB")
    private final ResponseType type;
    private final RoomUserType roomUserType;
    private final rVector3D pos;

    public SelectJobResponse(RoomUserType roomUserType, rVector3D pos) {
        this.type = ResponseType.SELECT_JOB;
        this.roomUserType = roomUserType;
        this.pos = pos;
    }
}
