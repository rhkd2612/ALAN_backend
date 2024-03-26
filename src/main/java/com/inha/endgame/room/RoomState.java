package com.inha.endgame.room;

public enum RoomState {
    NONE, // 대기방
    READY, // 시작 후 위치 선정, 역할 선정
    PLAY, // 게임 플레이
    END; // 종료
}
