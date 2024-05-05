package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.Date;

@Getter
public class RoomUserCrimeSpy extends RoomUserCrime {
    public RoomUserCrimeSpy(User user) {
        super(user);
    }

    public RoomUserCrimeSpy(RoomUser roomUser) {
        super(roomUser);
    }

    @Override
    public void playMission(int missionPhase, rVector3D missionPos) {
        super.playMission(missionPhase, missionPos);

        // 최종 미션
        if(missionPhase == MAX_MISSION_PHASE) {

        }
    }

    @Override
    public boolean clearMission(int missionPhase) {
        return super.clearMission(missionPhase);
    }

    @Override
    public CrimeType getCrimeType() {
        return CrimeType.SPY;
    }
}
