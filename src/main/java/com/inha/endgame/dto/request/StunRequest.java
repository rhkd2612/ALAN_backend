package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.core.io.RoomDelayRequest;
import com.inha.endgame.user.StunState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class StunRequest extends RoomDelayRequest {
    @Schema(description = "STUN", defaultValue = "STUN")
    private RequestType type;
    private String targetUsername;
    private Date targetingAt;
    private StunState stunState;
    private int roomId;
}
