package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class EventInfoResponse implements ClientResponse {
    @Schema(description = "EVENT_INFO", defaultValue = "EVENT_INFO")
    private final ResponseType type;
    private int animNum;
    private Date nextAnimAt;

    public EventInfoResponse(int animNum, Date nextAnimAt) {
        this.type = ResponseType.EVENT_INFO;
        this.animNum = animNum;
        this.nextAnimAt = nextAnimAt;
    }
}
