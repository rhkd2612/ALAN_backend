package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class LeaveRoomRequest implements ClientRequest {
    @Schema(description = "LEAVE_ROOM", defaultValue = "LEAVE_ROOM")
    private RequestType type;
    private String leaveUsername;
    private long roomId;
}
