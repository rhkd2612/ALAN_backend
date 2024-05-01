package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.Date;

@Getter
public class RoomUserCrime extends RoomUser {
    @JsonIgnore
    private int clearMissionPhase = 0;
    private static final int MAX_PHASE = 3;

    private Date firstMissionClearAt;
    private Date secondMissionClearAt;
    private Date thirdMissionClearAt;

    public RoomUserCrime(User user) {
        super(user);
    }

    public RoomUserCrime(RoomUser roomUser) {
        super(roomUser.getUsername(), roomUser.getNickname(), roomUser.getPos(), roomUser.getRot(), roomUser.getRoomUserType(), roomUser.getCrimeType());
    }

    public void playMission(int missionPhase, rVector3D missionPos) {
        // TODO원래 위치 검사도 해야한다 ㅇㅇ.. #4-12 합쳐지면 그때 처리
        if(missionPhase == this.clearMissionPhase + 1) {
            switch (this.getCrimeType()) {
                case SPY:
                    break;
                case BOOMER:
                    break;
            }
        }
    }

    public boolean clearMission(int missionPhase) {
        if(missionPhase == this.clearMissionPhase + 1) {
            switch (this.getCrimeType()) {
                case SPY:
                    break;
                case BOOMER:
                    break;
            }

            this.clearMissionPhase++;

            switch(this.clearMissionPhase) {
                case 1:
                    firstMissionClearAt = new Date();
                    break;
                case 2:
                    secondMissionClearAt = new Date();
                    break;
                case 3:
                    thirdMissionClearAt = new Date();
                    break;
            }

            // 우승~
            if(this.clearMissionPhase == MAX_PHASE)
                return true;
        }

        return false;
    }
}
