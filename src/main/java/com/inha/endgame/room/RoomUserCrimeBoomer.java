package com.inha.endgame.room;

import com.inha.endgame.core.excel.JsonReader;
import com.inha.endgame.core.excel.MapReader;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RoomUserCrimeBoomer extends RoomUserCrime {
    private Date boomAt = null;

    public RoomUserCrimeBoomer(User user) {
        super(user);
    }

    public RoomUserCrimeBoomer(RoomUser roomUser) {
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
        Date now = new Date();
        // 최초 클리어 시 15분 뒤 승리, 이후 미션 클리어 시 3분씩 감소
        if(missionPhase == 1)
            this.boomAt = new Date(now.getTime() + (1000 * 60 * JsonReader._int(JsonReader.model("boomerMinigame", "boomer_minigame", "timeToExplosion"))));
        else
            this.boomAt = new Date(this.boomAt.getTime() - JsonReader._int(JsonReader.model("boomerMinigame", "boomer_minigame", "reducedTimePerMissionClear")));

        return super.clearMission(missionPhase);
    }


    @Override
    public CrimeType getCrimeType() {
        return CrimeType.BOOMER;
    }

    @Override
    public Map<Integer, rVector3D> createRandomMissionPos() {
        Map<Integer, rVector3D> result = new HashMap<>();
        AtomicInteger missionIdx = new AtomicInteger(1);

        // 직업 미션 먼저 - 1개
        List<rVector3D> randomCrimeMissions = MapReader.getRandomCrimeMissions(this.getCrimeType());
        randomCrimeMissions.forEach(mission -> result.put(missionIdx.getAndIncrement(), mission));

        // 공통 미션 - 3개(완료 안해도 됨)
        List<rVector3D> randomCommonMissions = MapReader.getRandomCommonMissions(3);
        randomCommonMissions.forEach(mission -> result.put(missionIdx.getAndIncrement(), mission));

        return result;
    }
}
