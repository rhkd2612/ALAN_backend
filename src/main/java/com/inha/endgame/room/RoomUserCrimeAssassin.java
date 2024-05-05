package com.inha.endgame.room;

import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public class RoomUserCrimeAssassin extends RoomUserCrime {
    private final Set<String> targetUsernames = new HashSet<>();

    public RoomUserCrimeAssassin(User user) {
        super(user);
    }

    public RoomUserCrimeAssassin(RoomUser roomUser) {
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
        return CrimeType.ASSASSIN;
    }
}
