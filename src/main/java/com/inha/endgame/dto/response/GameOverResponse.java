package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.dto.GameOverInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class GameOverResponse implements ClientResponse {
    @Schema(description = "GAME_OVER", defaultValue = "GAME_OVER")
    private final ResponseType type;
    private final GameOverInfo info;

    public GameOverResponse(GameOverInfo info) {
        this.type = ResponseType.GAME_OVER;
        this.info = info;
    }
}
