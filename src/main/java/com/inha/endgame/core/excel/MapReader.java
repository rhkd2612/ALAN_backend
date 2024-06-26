package com.inha.endgame.core.excel;

import com.inha.endgame.room.*;
import com.inha.endgame.user.CrimeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
public class MapReader {
    private final JsonReader jsonReader;
    private final ExcelParser excelParser;
    private List<List<Tile>> gameMap;

    private static rVector3D copSpawnPos;
    private static final List<rVector3D> crimeSpawnPoses = new ArrayList<>();
    private static final List<rVector3D> crimeCommonMissionPoses = new ArrayList<>();
    private static final List<rVector3D> crimeSpySpecificMissionPoses = new ArrayList<>();
    private static rVector3D crimeBoomerSpecificMissionPos;

    public MapReader(ExcelParser excelParser, JsonReader jsonReader) throws IOException {
        this.excelParser = excelParser;
        this.jsonReader = jsonReader;

        var fileName = "map";
        var key = "map_size";

        RoomService.minX = JsonReader._int(JsonReader.model(fileName, key, "mapXmin"));
        RoomService.maxX = JsonReader._int(JsonReader.model(fileName, key, "mapXmax"));
        RoomService.minZ = JsonReader._int(JsonReader.model(fileName, key, "mapZmin"));
        RoomService.maxZ = JsonReader._int(JsonReader.model(fileName, key, "mapZmax"));

        File curDir = this.excelParser.getProjectDirectory();
        this.gameMap = this.excelParser.convertExcelToMap(curDir.getParent() + "/model/Stage.xlsx", (int)RoomService.maxX, (int)RoomService.maxZ);

        for(int i = 0; i < this.gameMap.size(); i++) {
            for(int j = 0; j < this.gameMap.get(i).size(); j++) {
                Tile currentTile = this.gameMap.get(i).get(j);
                rVector3D currentPos = new rVector3D(i, 0, j);

                if(currentTile == Tile.COP_SPAWN)
                    copSpawnPos = currentPos;
                else if(currentTile == Tile.SPAWN) {
                    if(this.check(currentPos, 2))
                        crimeSpawnPoses.add(currentPos);
                }
                else if(currentTile == Tile.MISSION)
                    crimeCommonMissionPoses.add(currentPos);
                else if(currentTile == Tile.MISSION_SPY)
                    crimeSpySpecificMissionPoses.add(currentPos);
                else if(currentTile == Tile.MISSION_BOOMER)
                    crimeBoomerSpecificMissionPos = currentPos;
            }
        }
    }

    public Tile getTile(float x, float z) {
        int intx = Math.round(x);
        int intz = Math.round(z);

        intx = Math.max(0, intx);
        intx = Math.min(150, intx);

        intz = Math.max(0, intz);
        intz = Math.min(140, intz);

        return gameMap.get(intx).get(intz);
    }

    public List<Tile> getRouteTiles(rVector3D startPos, rVector3D predictPos) {
        List<Tile> result = new ArrayList<>();

        float dx = predictPos.getX() - startPos.getX();
        float dz = predictPos.getZ() - startPos.getZ();

        float stepX = dx / 10;
        float stepZ = dz / 10;

        float currentX = startPos.getX();
        float currentZ = startPos.getZ();

        for (int i = 1; i <= 10; i++) {
            result.add(getTile(currentX, currentZ));

            // 다음 위치로 이동
            currentX += stepX;
            currentZ += stepZ;
        }

        result.add(getTile(predictPos.getX(), predictPos.getZ()));

        return result;
    }

    public boolean check(rVector3D nextPos, int safeRange) {
        Tile targetTile = getTile(nextPos.getX(), nextPos.getZ());

        int[] dx = {0, -1, 0, 1, -1, 1, 1, -1};
        int[] dz = {-1, 0, 1, 0, 1, -1, 1, -1};

        for (int i = 0; i < 8; i++) {
            for (int j = 1; j <= safeRange; j++) {
                Tile predictTile = getTile(nextPos.getX() + dx[i] * j, nextPos.getZ() + dz[i] * j);
                if (!predictTile.isCanMove())
                    return false;
            }
        }

        return targetTile.isCanMove();
    }

    public boolean check(rVector3D startPos, rVector3D predictPos) {
        var targetTiles = getRouteTiles(startPos, predictPos);
        for(var targetTile : targetTiles) {
            if(!targetTile.isCanMove())
                return false;
        }
        return true;
    }

    public static rVector3D getCopSpawnPos() {
        return copSpawnPos;
    }

    public static rVector3D getRandomCrimePos() {
        int r = RandomUtils.nextInt(0, crimeSpawnPoses.size());
        return crimeSpawnPoses.get(r);
    }

    public List<rVector3D> getRandomNpcPos(int count) {
        List<rVector3D> npcPos = new ArrayList<>();

        var npcSpawnMinX = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer", "posXmin"));
        var npcSpawnMaxX = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer", "posXmax"));
        var npcSpawnMinZ = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer", "posZmin"));
        var npcSpawnMaxZ = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer", "posZmax"));

        for (int i = 0; i < count; i++) {
            var pos = new rVector3D(npcSpawnMinX + (float) (Math.random() * (npcSpawnMaxX - npcSpawnMinX)), 0, npcSpawnMinZ + (float) (Math.random() * (npcSpawnMaxZ - npcSpawnMinZ)));
            while(!check(pos, 2))
                pos = new rVector3D(npcSpawnMinX + (float) (Math.random() * (npcSpawnMaxX - npcSpawnMinX)), 0, npcSpawnMinZ + (float) (Math.random() * (npcSpawnMaxZ - npcSpawnMinZ)));
            npcPos.add(pos);
        }

        return npcPos;
    }

    public static List<rVector3D> getRandomCommonMissions(int count) {
        CopyOnWriteArrayList<rVector3D> copyArray = new CopyOnWriteArrayList<>(crimeCommonMissionPoses);

        List<rVector3D> missions = new ArrayList<>();
        int missionCount = 0;
        while(count-- > 0) {
            var r = RandomUtils.nextInt(0, copyArray.size() - missionCount);
            var missionPos = copyArray.get(r);

            rVector3D temp = copyArray.get(copyArray.size() - missionCount - 1);
            copyArray.set(copyArray.size() - missionCount - 1, missionPos);
            copyArray.set(r, temp);

            missions.add(missionPos);

            missionCount++;
        }

        return missions;
    }

    public static List<rVector3D> getRandomCrimeMissions(CrimeType crimeType) {
        List<rVector3D> missions = new ArrayList<>();

        switch(crimeType) {
            case SPY: {
                var r = RandomUtils.nextInt(0, crimeSpySpecificMissionPoses.size());
                var missionPos = crimeSpySpecificMissionPoses.get(r);

                missions.add(missionPos);
                break;
            }
            case BOOMER: {
                missions.add(crimeBoomerSpecificMissionPos);
                break;
            }
            case ASSASSIN: {
                throw new IllegalArgumentException("어쌔신은 맵에서 받아오지 않는다.");
            }
        }

        return missions;
    }
}