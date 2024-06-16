package com.inha.endgame.dto;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.UserState;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class LobbyRoomDto {
    private long roomId;
    private List<RoomUser> roomUsers;
    private List<RoomUser> roomNpcs;
    private RoomState roomState;

    public LobbyRoomDto(Room room) {
        this.roomId = room.getRoomId();
        this.roomUsers = new ArrayList<>(room.getAllRoomUsersByRoomIdOrderByCop());
        this.roomNpcs = new ArrayList<>(room.getRoomNpcs().values());
        this.roomState = room.getCurState();
    }
}
