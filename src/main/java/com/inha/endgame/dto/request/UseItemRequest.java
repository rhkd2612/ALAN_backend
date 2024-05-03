package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UseItemRequest implements ClientRequest {
    @Schema(description = "USE_ITEM", defaultValue = "USE_ITEM")
    private RequestType type;
    private rVector3D itemPos;
    private String username;
    private long roomId;
}