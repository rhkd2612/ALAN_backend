package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.core.io.RoomDelayResponse;
import com.inha.endgame.dto.request.PlayMissionRequest;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.MissionState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class PlayMissionResponse extends RoomDelayResponse {
    @Schema(description = "PLAY_MISSION", defaultValue = "PLAY_MISSION")
    private final ResponseType type;
    private final long roomId;
    private final int missionPhase;
    private final rVector3D missionPos;
    private final CrimeType crimeType;
    private final String username;
    private final MissionState missionState;

    public PlayMissionResponse(PlayMissionRequest request) {
        this.type = ResponseType.PLAY_MISSION;
        this.roomId = request.getRoomId();
        this.missionPhase = request.getMissionPhase();
        this.missionPos = request.getMissionPos();
        this.crimeType = request.getCrimeType();
        this.username = request.getUsername();
        this.missionState = request.getMissionState();
    }
}
