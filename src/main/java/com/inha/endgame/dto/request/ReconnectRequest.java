package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReconnectRequest implements ClientRequest {
    @Schema(description = "RECONNECT", defaultValue = "RECONNECT")
    private RequestType type;
    private String username;
    private long roomId;
}
