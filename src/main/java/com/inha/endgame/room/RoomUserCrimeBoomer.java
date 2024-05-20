package com.inha.endgame.room;

import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

@Getter
public class RoomUserCrimeBoomer extends RoomUserCrime {
    public RoomUserCrimeBoomer(User user) {
        super(user);
    }

    public RoomUserCrimeBoomer(RoomUser roomUser) {
        super(roomUser);
    }

    @Override
    public void setMaxMissionPhase() {
        this.maxMissionPhase = 3;
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
        return CrimeType.BOOMER;
    }
}
