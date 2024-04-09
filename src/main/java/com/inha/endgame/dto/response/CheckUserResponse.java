
package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CheckUserResponse implements ClientResponse {
    @Schema(description = "CHECK_USER", defaultValue = "CHECK_USER")
    private final ResponseType type;
    private final boolean isExist;
    private final String nickname;

    public CheckUserResponse(boolean isExist, String nickname) {
        this.type = ResponseType.CHECK_USER;
        this.isExist = isExist;
        this.nickname = nickname;
    }
}
