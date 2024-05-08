package com.inha.endgame.dto;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import lombok.Data;

@Data
public class RoomDto {
    private long roomId;
    private String hostNickname;
    private int userCount;
    private int maxUserCount;
    private RoomState roomState;

    public RoomDto(Room room) {
        this.roomId = room.getRoomId();
        this.hostNickname = room.getHostNickname();
        this.userCount = room.getRoomUsers().size();
        this.maxUserCount = 4;
        this.roomState = room.getCurState();
    }
}
