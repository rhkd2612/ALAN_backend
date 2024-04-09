package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class NetworkDelayRequest implements ClientRequest {
    @Schema(description = "NETWORK_DELAY", defaultValue = "NETWORK_DELAY")
    private RequestType type;
    private int networkDelay;
    private int networkBounce;
}
