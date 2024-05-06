package com.inha.endgame.core.excel;

import com.inha.endgame.room.RoomUserCrime;
import com.inha.endgame.room.Tile;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.CrimeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class MapReader {
    private final ExcelParser excelParser;
    private List<List<Tile>> gameMap;

    private static rVector3D copSpawnPos;
    private static final List<rVector3D> crimeSpawnPoses = new ArrayList<>();
    private static final List<rVector3D> crimeCommonMissionPoses = new ArrayList<>();
    private static final List<rVector3D> crimeSpySpecificMissionPoses = new ArrayList<>();
    private static final List<rVector3D> crimeBoomerSpecificMissionPoses = new ArrayList<>();

    public MapReader(ExcelParser excelParser) throws IOException {
        this.excelParser = excelParser;

        File curDir = this.excelParser.getProjectDirectory();
        this.gameMap = this.excelParser.convertExcelToMap(curDir.getParent() + "/Client/Stage.xlsx", 100);

        for(int i = 0; i < this.gameMap.size(); i++) {
            for(int j = 0; j < this.gameMap.get(i).size(); j++) {
                Tile currentTile = this.gameMap.get(i).get(j);
                rVector3D currentPos = new rVector3D(100 - i, 0, 100 - j);

                if(currentTile == Tile.COP_SPAWN)
                    copSpawnPos = currentPos;
                else if(currentTile == Tile.SPAWN)
                    crimeSpawnPoses.add(currentPos);
                else if(currentTile == Tile.MISSION)
                    crimeCommonMissionPoses.add(currentPos);
                else if(currentTile == Tile.MISSION_SPY)
                    crimeSpySpecificMissionPoses.add(currentPos);
                else if(currentTile == Tile.MISSION_BOOMER)
                    crimeBoomerSpecificMissionPoses.add(currentPos);
            }
        }
    }

    public Tile getTile(float x, float z) {
        int intx = 100 - (int) Math.floor(x);
        int intz = 100 - (int) Math.floor(z);

        intx = Math.max(0, intx);
        intx = Math.min(99, intx);

        intz = Math.max(0, intz);
        intz = Math.min(99, intz);

        return gameMap.get(intx).get(intz);
    }

    public boolean check(rVector3D nextPos) {
        Tile targetTile = getTile(nextPos.getX(), nextPos.getZ());
        return targetTile.isCanMove();
    }

    public static rVector3D getCopSpawnPos() {
        return copSpawnPos;
    }

    public static rVector3D getRandomCrimePos() {
        int r = RandomUtils.nextInt(0, crimeSpawnPoses.size());
        return crimeSpawnPoses.get(r);
    }

    public static Map<Integer, rVector3D> getRandomCrimeMissionPos(CrimeType crimeType) {
        CopyOnWriteArrayList<rVector3D> copyArray = new CopyOnWriteArrayList<>(crimeCommonMissionPoses);

        Map<Integer, rVector3D> missions = new HashMap<>();
        int missionCount = 0;
        while(true) {
            var r = RandomUtils.nextInt(0, copyArray.size() - missionCount);
            var missionPos = copyArray.get(r);

            rVector3D temp = copyArray.get(copyArray.size() - missionCount - 1);
            copyArray.set(copyArray.size() - missionCount - 1, missionPos);
            copyArray.set(r, temp);

            missions.put(++missionCount, missionPos);

            if(missionCount >= RoomUserCrime.MAX_COMMON_MISSION_PHASE)
                break;
        }

        switch(crimeType) {
            case SPY: {
                var r = RandomUtils.nextInt(0, crimeSpySpecificMissionPoses.size());
                var missionPos = crimeSpySpecificMissionPoses.get(r);

                missions.put(++missionCount, missionPos);
                break;
            }
            case BOOMER: {
                var r = RandomUtils.nextInt(0, crimeBoomerSpecificMissionPoses.size());
                var missionPos = crimeBoomerSpecificMissionPoses.get(r);

                missions.put(++missionCount, missionPos);
                break;
            }
        }

        return missions;
    }
}