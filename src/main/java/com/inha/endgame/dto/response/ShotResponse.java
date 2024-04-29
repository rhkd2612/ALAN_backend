package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class ShotResponse implements ClientResponse {
    @Schema(description = "SHOT", defaultValue = "SHOT")
    private final ResponseType type;
    private final String targetUsername;
    private final RoomUserType targetUserType;
    @Schema(description = "남은 유저 수")
    private final int aliveUserCount;
    private final Date stunAvailAt;

    public ShotResponse(String targetUsername, RoomUserType targetUserType, int aliveUserCount, Date stunAvailAt) {
        this.type = ResponseType.SHOT;
        this.targetUsername = targetUsername;
        this.targetUserType = targetUserType;
        this.aliveUserCount = aliveUserCount;
        this.stunAvailAt = stunAvailAt;
    }
}