package com.inha.endgame.room;

public enum Tile {
    GROUND(0, true),
    WALL(1, false),
    BUILDING(2, false),
    SPAWN(3, true),
    MISSION(4, true);

    final int tileNum;
    final boolean canMove;

    Tile(int tileNum, boolean canMove) {
        this.tileNum = tileNum;
        this.canMove = canMove;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public static Tile toTile(String num) {
        if (num.equals("0") || num.equals("0.0"))
            return GROUND;
        else if (num.equals("1") || num.equals("1.0"))
            return WALL;
        else if (num.equals("2") || num.equals("2.0"))
            return BUILDING;
        else if (num.equals("3") || num.equals("3.0"))
            return SPAWN;
        else if (num.equals("4") || num.equals("4.0"))
            return MISSION;

        throw new IllegalArgumentException("정의되지 않은 맵 값이 사용");
    }
}