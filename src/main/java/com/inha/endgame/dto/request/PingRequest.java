package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

// heartbeat + 시간 동기화
@Getter
public class PingRequest implements ClientRequest {
    @Schema(description = "PING", defaultValue = "PING")
    private RequestType type;
}