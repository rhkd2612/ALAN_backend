package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.core.io.RoomDelayRequest;
import com.inha.endgame.user.AimState;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class AimRequest extends RoomDelayRequest {
    @Schema(description = "AIM", defaultValue = "AIM")
    private RequestType type;
    private rVector3D aimPos;
    private Date aimAt;
    private int roomId;
    private AimState aimState;
}