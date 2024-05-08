
package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class RoomListRequest implements ClientRequest {
    @Schema(description = "ROOM_LIST", defaultValue = "ROOM_LIST")
    private RequestType type;
}
