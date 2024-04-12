package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.user.AimState;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class AimResponse implements ClientResponse {
    @Schema(description = "AIM", defaultValue = "AIM")
    private final ResponseType type;
    private final rVector3D aimPos;
    private final Date aimAt;
    private final AimState aimState;

    public AimResponse(rVector3D aimPos, Date aimAt, AimState aimState) {
        this.type = ResponseType.AIM;
        this.aimPos = aimPos;
        this.aimAt = aimAt;
        this.aimState = aimState;
    }
}