package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class StunRequest implements ClientRequest {
    @Schema(description = "STUN", defaultValue = "STUN")
    private RequestType type;
    private String targetUsername;
    private Date targetingAt;
    private int roomId;
}
