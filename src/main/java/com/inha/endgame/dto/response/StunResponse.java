package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class StunResponse implements ClientResponse {
    @Schema(description = "STUN", defaultValue = "STUN")
    private final ResponseType type;
    private final String targetUsername;
    private final Date availShotAt;
    private final Date nextStunAvailAt;

    public StunResponse(String targetUsername, Date availShotAt, Date nextStunAvailAt) {
        this.type = ResponseType.STUN;
        this.targetUsername = targetUsername;
        this.availShotAt = availShotAt;
        this.nextStunAvailAt = nextStunAvailAt;
    }
}