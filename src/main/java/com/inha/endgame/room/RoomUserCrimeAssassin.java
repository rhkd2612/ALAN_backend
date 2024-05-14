package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.User;
import lombok.Getter;

import javax.persistence.JoinColumn;
import java.util.*;

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
        this.maxMissionPhase = 3;
    }

    public void addTarget(String targetUsername) {
        targetUsernames.add(targetUsername);
    }

    public int killTarget(String username) {
        targetUsernames.remove(username);
        return targetUsernames.size();
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

    public List<String> getTargetInfo() {
        return new ArrayList<>(targetUsernames);
    }

    @Override
    public CrimeType getCrimeType() {
        return CrimeType.ASSASSIN;
    }
}
