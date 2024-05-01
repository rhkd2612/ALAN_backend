package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.room.RoomUserType;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.CrimeType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SelectJobResponse implements ClientResponse {
    @Schema(description = "SELECT_JOB", defaultValue = "SELECT_JOB")
    private final ResponseType type;
    private final RoomUserType roomUserType;
    private final rVector3D pos;
    private final CrimeType crimeType;

    private final List<RoomUser> roomUsers = new ArrayList<>();;

    public SelectJobResponse(RoomUserType roomUserType, rVector3D pos, CrimeType crimeType, List<RoomUser> roomUsers) {
        this.type = ResponseType.SELECT_JOB;
        this.roomUserType = roomUserType;
        this.pos = pos;
        this.crimeType = crimeType;
        this.roomUsers.addAll(roomUsers);
    }
}
