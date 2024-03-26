package com.inha.endgame.room;


import lombok.Getter;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Room {
    private final long roomId;
    private final Date createAt;
    private final Map<String, RoomUser> roomUsers;
    private RoomState curState;
    private RoomState nextState;

    public Room(long roomId) {
        this.roomId = roomId;
        this.createAt = new Date();
        this.roomUsers = new ConcurrentHashMap<>();
        this.curState = null;
        this.nextState = RoomState.NONE;
    }

    public synchronized void join(RoomUser user) {
        if(this.curState == RoomState.END)
            throw new IllegalStateException("종료된 방입니다.");
        if(this.roomUsers.containsKey(user.getUsername())) {
            // 재접속 로직
            return;
        }

        this.roomUsers.put(user.getUsername(), user);
    }

    public synchronized void start() {
        if(this.curState != RoomState.NONE)
            throw new IllegalStateException("시작할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.READY;
    }

    public synchronized void kick(RoomUser user) {
        this.roomUsers.remove(user.getUsername());
    }

    // 일반적으로는 변경하면 안됨
    public void setCurState(RoomState curState) {
        this.curState = curState;
    }

    public void setNextState(RoomState nextState) {
        this.nextState = nextState;
    }
}
