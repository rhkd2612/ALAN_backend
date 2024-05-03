package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.rVector3D;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UseItemResponse implements ClientResponse {
    @Schema(description = "USE_ITEM", defaultValue = "USE_ITEM")
    private final ResponseType type;
    private final rVector3D itemPos;

    public UseItemResponse(rVector3D itemPos) {
        this.type = ResponseType.USE_ITEM;
        this.itemPos = itemPos;
    }
}
