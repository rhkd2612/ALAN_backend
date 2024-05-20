package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class AssassinKillResponse implements ClientResponse {
    @Schema(description = "ASSASSIN_KILL", defaultValue = "ASSASSIN_KILL")
    private final ResponseType type;
    private final String targetUsername;

    public AssassinKillResponse(String targetUsername) {
        this.type = ResponseType.ASSASSIN_KILL;
        this.targetUsername = targetUsername;
    }
}
