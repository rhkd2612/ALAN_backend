package com.inha.endgame.core.excel;

import com.inha.endgame.room.Tile;
import com.inha.endgame.room.rVector3D;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MapReader {
    private final ExcelParser excelParser;
    private List<List<Tile>> gameMap;

    private static rVector3D copSpawnPos;
    private static final List<rVector3D> crimeSpawnPoses = new ArrayList<>();

    public MapReader(ExcelParser excelParser) throws IOException {
        this.excelParser = excelParser;

        File curDir = this.excelParser.getProjectDirectory();
        this.gameMap = this.excelParser.convertExcelToMap(curDir.getParent() + "/Client/Stage.xlsx", 100);

        for(int i = 0; i < this.gameMap.size(); i++) {
            for(int j = 0; j < this.gameMap.get(i).size(); j++) {
                Tile currentTile = this.gameMap.get(i).get(j);

                if(currentTile == Tile.COP_SPAWN)
                    copSpawnPos = new rVector3D(100 - i, 0 , 100 - j);
                else if(currentTile == Tile.SPAWN)
                    crimeSpawnPoses.add(new rVector3D(100 - i, 0, 100 - j));
            }
        }
    }

    public boolean check(rVector3D nextPos) {
        Tile targetTile = gameMap.get(100 - (int) Math.floor(nextPos.getX())).get(100 - (int) Math.floor(nextPos.getZ()));
        return targetTile.isCanMove();
    }

    public static rVector3D getCopSpawnPos() {
        return copSpawnPos;
    }

    public static rVector3D getRandomCrimePos() {
        int r = RandomUtils.nextInt(0, crimeSpawnPoses.size());
        return crimeSpawnPoses.get(r);
    }
}