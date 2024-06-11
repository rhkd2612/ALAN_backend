package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.core.io.RoomDelayRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class ShotRequest extends RoomDelayRequest {
    @Schema(description = "SHOT", defaultValue = "SHOT")
    private RequestType type;
    private Date shotAt;
    private int roomId;
}