package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class SettingRoomRequest implements ClientRequest {
	@Schema(description = "SETTING_ROOM", defaultValue = "SETTING_ROOM")
	private RequestType type;
	private long roomId;
	private int npcCount;
	private float npcMinSpawnX;
	private float npcMaxSpawnX;
	private float npcMinSpawnZ;
	private float npcMaxSpawnZ;
}
