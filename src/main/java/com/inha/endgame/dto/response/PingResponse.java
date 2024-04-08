package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class PingResponse implements ClientResponse {
    @Schema(description = "PING", defaultValue = "PING")
    private final ResponseType type;
    private final Date serverTime;

    public PingResponse(Date serverTime) {
        this.type = ResponseType.PING;
        this.serverTime = serverTime;
    }
}