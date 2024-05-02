package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.CrimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlayMissionRequest implements ClientRequest {
	@Schema(description = "PLAY_MISSION", defaultValue = "PLAY_MISSION")
	private RequestType type;
	private long roomId;
	private int missionPhase;
	private rVector3D missionPos;
	private CrimeType crimeType;
	private String username;
	private boolean clear;
}
