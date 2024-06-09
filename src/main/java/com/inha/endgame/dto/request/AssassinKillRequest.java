package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.core.io.RoomDelayRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AssassinKillRequest extends RoomDelayRequest {
    @Schema(description = "ASSASSIN_KILL", defaultValue = "ASSASSIN_KILL")
    private RequestType type;
    private String targetUsername;
    private long roomId;
}