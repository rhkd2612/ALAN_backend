package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.room.RoomUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UpdateUserRequest implements ClientRequest {
	@Schema(description = "UPDATE_USER", defaultValue = "UPDATE_USER")
	private RequestType type;
	private RoomUser roomUser;
	private long roomId;
}
