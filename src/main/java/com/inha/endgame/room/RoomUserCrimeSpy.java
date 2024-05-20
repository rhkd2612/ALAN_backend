package com.inha.endgame.room;

import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

@Getter
public class RoomUserCrimeSpy extends RoomUserCrime {
    public RoomUserCrimeSpy(User user) {
        super(user);
    }

    public RoomUserCrimeSpy(RoomUser roomUser) {
        super(roomUser);
    }

    @Override
    public void setMaxMissionPhase() {
        this.maxMissionPhase = 4;
    }

    @Override
    public void playMission(int missionPhase, rVector3D missionPos) {
        super.playMission(missionPhase, missionPos);

        // 최종 미션
        if(missionPhase == this.maxMissionPhase) {

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
