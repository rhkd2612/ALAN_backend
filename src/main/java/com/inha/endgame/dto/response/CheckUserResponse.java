
package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CheckUserResponse implements ClientResponse {
    @Schema(description = "CHECK_USER", defaultValue = "CHECK_USER")
    private final ResponseType type;
    private final boolean isExist;

    public CheckUserResponse(boolean isExist) {
        this.type = ResponseType.ADD_USER;
        this.isExist = isExist;
    }
}
