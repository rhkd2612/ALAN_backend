package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
public class LeaveRoomResponse implements ClientResponse {
    @Schema(description = "LEAVE_ROOM", defaultValue = "LEAVE_ROOM")
    private final ResponseType type;
    private final List<String> leaveUsername;
    private boolean checkLeave;

    public LeaveRoomResponse(List<String> leaveUsername, boolean checkLeave) {
        this.type = ResponseType.LEAVE_ROOM;
        this.leaveUsername = leaveUsername;
        this.checkLeave = checkLeave;
    }
}