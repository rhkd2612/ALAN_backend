package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AssassinKillRequest implements ClientRequest {
    @Schema(description = "ASSASSIN_KILL", defaultValue = "ASSASSIN_KILL")
    private RequestType type;
    private String targetUsername;
    private long roomId;
}