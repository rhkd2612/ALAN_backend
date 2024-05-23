
package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class GameOverConfirmRequest implements ClientRequest {
	@Schema(description = "GAME_OVER_CONFIRM", defaultValue = "GAME_OVER_CONFIRM")
	private RequestType type;
	private String username;
	private long roomId;
}
