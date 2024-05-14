package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.core.excel.MapReader;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
public abstract class RoomUserCrime extends RoomUser {
    @JsonIgnore
    protected int clearMissionPhase = 0;
    @JsonIgnore
    public static final int MAX_COMMON_MISSION_PHASE = 2;

    @JsonIgnore
    protected List<Date> missionClearAt;

    @JsonIgnore
    protected int maxMissionPhase;
    @JsonIgnore
    protected int remainItemCount = 3;

    @JsonIgnore
    protected Map<Integer, rVector3D> missionPos;

    public RoomUserCrime(User user) {
        super(user);
    }

    public RoomUserCrime(RoomUser roomUser) {
        super(roomUser.getUsername(), roomUser.getNickname(), roomUser.getPos(), roomUser.getRot(), roomUser.getRoomUserType(), roomUser.getCrimeType());

        this.missionPos = MapReader.getRandomCrimeMissionPos(this.getCrimeType());
        setMaxMissionPhase();
    }

    public abstract void setMaxMissionPhase();

    public static RoomUserCrime createCrime(RoomUser roomUser, CrimeType crimeType) {
        switch(crimeType) {
            case SPY:
                return new RoomUserCrimeSpy(roomUser);
            case BOOMER:
                return new RoomUserCrimeBoomer(roomUser);
            case ASSASSIN:
                return new RoomUserCrimeAssassin(roomUser);
        }

        throw new IllegalStateException("범죄자 인원 초과");
    }

    public void playMission(int missionPhase, rVector3D missionPos) {
        if(missionPhase == this.clearMissionPhase + 1) {
            // 공통 미션 처리는 여기서
            if(missionPhase <= MAX_COMMON_MISSION_PHASE) {

            }
        }
    }

    public synchronized void useItem() {
        if(this.remainItemCount < 1)
            throw new IllegalStateException("아이템 개수 부족");

        this.remainItemCount--;
    }

    public boolean clearMission(int missionPhase) {
        if(missionPhase == this.clearMissionPhase + 1) {
            this.clearMissionPhase++;
            this.missionClearAt.add(new Date());

            // 우승~
            if(this.clearMissionPhase == this.maxMissionPhase)
                return true;
        }

        return false;
    }

    public abstract CrimeType getCrimeType();
}
