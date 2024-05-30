package com.inha.endgame.room;

public enum Tile {
    GROUND(0, true),
    WALL(1, false),
    BUILDING(2, false),
    MISSION(3, true),
    MISSION_SPY(4, true),
    MISSION_THIEF(5, true),
    MISSION_BOOMER(6, true),
    SPAWN(7, true),
    COP_SPAWN(8, true);

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
            return MISSION;
        else if (num.equals("4") || num.equals("4.0"))
            return MISSION_SPY;
        else if (num.equals("5") || num.equals("5.0"))
            return MISSION_THIEF;
        else if (num.equals("6") || num.equals("6.0"))
            return MISSION_BOOMER;
        else if (num.equals("7") || num.equals("7.0"))
            return SPAWN;
        else if (num.equals("8") || num.equals("8.0"))
            return COP_SPAWN;


        throw new IllegalArgumentException("정의되지 않은 맵 값이 사용");
    }
}