package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.core.excel.MapReader;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RoomUserCrimeAssassin extends RoomUserCrime {
    @JsonIgnore
    private final Set<String> targetUsernames = new HashSet<>();

    public RoomUserCrimeAssassin(User user) {
        super(user);
    }

    public RoomUserCrimeAssassin(RoomUser roomUser) {
        super(roomUser);
    }

    @Override
    public void setMaxMissionPhase() {
        this.maxMissionPhase = 5;
    }

    public void addTarget(String targetUsername) {
        targetUsernames.add(targetUsername);
    }

    public int killTarget(String username) {
        targetUsernames.remove(username);
        return targetUsernames.size();
    }

    @Override
    public void clearMission(int missionPhase) {
        super.clearMission(missionPhase);
    }

    public List<String> getTargetInfo() {
        return new ArrayList<>(targetUsernames);
    }

    @Override
    public CrimeType getCrimeType() {
        return CrimeType.ASSASSIN;
    }

    @Override
    public Map<Integer, rVector3D> createRandomMissionPos() {
        Map<Integer, rVector3D> result = new HashMap<>();
        AtomicInteger missionIdx = new AtomicInteger(1);

        // 공통 미션 먼저
        List<rVector3D> randomCommonMissions = MapReader.getRandomCommonMissions(2);
        randomCommonMissions.forEach(mission -> result.put(missionIdx.getAndIncrement(), mission));

        return result;
    }
}
