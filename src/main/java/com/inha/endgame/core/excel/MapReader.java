package com.inha.endgame.core.excel;

import com.inha.endgame.room.Tile;
import com.inha.endgame.room.rVector3D;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class MapReader {
    private final ExcelParser excelParser;
    private List<List<Tile>> gameMap;

    public MapReader(ExcelParser excelParser) throws IOException {
        this.excelParser = excelParser;

        File curDir = this.excelParser.getProjectDirectory();
        this.gameMap = this.excelParser.convertExcelToMap(curDir.getParent() + "/Client/Stage.xlsx", 100);
    }

    public boolean check(rVector3D nextPos) {
        Tile targetTile = gameMap.get(100 - (int) Math.floor(nextPos.getX())).get(100 - (int) Math.floor(nextPos.getZ()));
        return targetTile.isCanMove();
    }
}