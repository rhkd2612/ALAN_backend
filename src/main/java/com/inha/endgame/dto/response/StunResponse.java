package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.core.io.RoomDelayResponse;
import com.inha.endgame.user.StunState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class StunResponse extends RoomDelayResponse {
    @Schema(description = "STUN", defaultValue = "STUN")
    private final ResponseType type;
    private final String targetUsername;
    private final Date availShotAt;
    private final Date nextStunAvailAt;
    private final StunState stunState;

    public StunResponse(String targetUsername, Date availShotAt, Date nextStunAvailAt, StunState stunState) {
        this.type = ResponseType.STUN;
        this.targetUsername = targetUsername;
        this.availShotAt = availShotAt;
        this.nextStunAvailAt = nextStunAvailAt;
        this.stunState = stunState;
    }
}