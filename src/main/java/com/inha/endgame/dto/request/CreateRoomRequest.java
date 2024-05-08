
package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class CreateRoomRequest implements ClientRequest {
    @Schema(description = "CREATE_ROOM", defaultValue = "CREATE_ROOM")
    private RequestType type;
}
