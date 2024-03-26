package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class StartRoomRequest implements ClientRequest {
	@Schema(description = "START_ROOM", defaultValue = "START_ROOM")
	private RequestType type;
	private long roomId;
}
