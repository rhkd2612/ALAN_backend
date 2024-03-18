package com.inha.endgame.room;


import com.inha.endgame.user.RoomUser;
import lombok.Getter;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Room {
    private long roomId;
    private Date createAt;
    private Map<String, RoomUser> roomUsers;
    private RoomState state;

    // RoomState

    public Room() {
        this.roomId = 1; // 일단 1만 사용
        this.createAt = new Date();
        this.roomUsers = new ConcurrentHashMap<>();
        this.state = RoomState.READY;
    }

    public void join(RoomUser user) {
        if(this.state == RoomState.END)
            throw new IllegalStateException("종료된 방입니다.");
        if(this.roomUsers.containsKey(user.getUserId()))
            throw new IllegalStateException("이미 참여한 방입니다.");

        this.roomUsers.put(user.getUserId(), user);
    }
}
