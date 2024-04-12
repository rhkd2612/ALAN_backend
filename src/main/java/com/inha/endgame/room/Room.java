package com.inha.endgame.room;


import com.inha.endgame.room.event.AnimEvent;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class Room {
    private final long roomId;
    private final Map<String, RoomUser> roomUsers;
    private final Map<String, RoomUser> roomNpcs;
    private String copUsername;
    private AnimEvent event;

    private RoomState curState;
    private RoomState nextState;

    private final Date createAt;
    private Date readyAt;

    public static float minX;
    public static float minZ;
    public static float maxX;
    public static float maxZ;


    public Room(long roomId) {
        this.roomId = roomId;
        this.createAt = new Date();
        this.roomUsers = new ConcurrentHashMap<>();
        this.roomNpcs = new ConcurrentHashMap<>();
        this.curState = null;
        this.nextState = RoomState.NONE;
    }

    public static void setRoomMapSize(float minX, float minZ, float maxX, float maxZ) {
        Room.minX = minX;
        Room.minZ = minZ;
        Room.maxX = maxX;
        Room.maxZ = maxZ;
    }

    public void setCopUsername(String copUsername) {
        this.copUsername = copUsername;
    }

    public List<RoomUser> getAllMembers() {
        List<RoomUser> result = new ArrayList<>();

        result.addAll(this.roomUsers.values());
        result.addAll(this.roomNpcs.values());

        return result;
    }

    public Map<String, RoomUser> getAllMembersMap() {
        Map<String,RoomUser> result = new HashMap<>();

        result.putAll(this.roomUsers);
        result.putAll(this.roomNpcs);

        return result;
    }

    public RoomUserCop getCop() {
        return (RoomUserCop)(this.roomUsers.get(this.copUsername));
    }

    public void setEvent(AnimEvent event) {
        this.event = event;
    }

    public void setRoomNpc(int npcCount) {
        if(this.curState != RoomState.NONE)
            throw new IllegalStateException("NONE 상태일 때만 세팅 가능합니다.");

        this.roomNpcs.clear();

        List<RoomUser> npcs = RoomUser.createNpc(npcCount);
        npcs.forEach(npc -> this.roomNpcs.put(npc.getUsername(), npc));
    }

    public synchronized void join(RoomUser user) {
        if(this.curState == RoomState.END)
            throw new IllegalStateException("종료된 방입니다.");
        if(this.roomUsers.containsKey(user.getUsername())) {
            return;
        }

        this.roomUsers.put(user.getUsername(), user);
    }

    public synchronized void start() {
        if(this.curState != RoomState.NONE)
            throw new IllegalStateException("시작할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.READY;
        this.readyAt = new Date();
    }

    public Date getPlayAt() {
        return new Date(this.readyAt.getTime() + 5000);
    }

    public Date getEndAt() {
        return new Date(this.readyAt.getTime() + 1000000);
    }

    public synchronized void play() {
        if(this.curState != RoomState.READY)
            throw new IllegalStateException("시작할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.PLAY;
    }

    public synchronized void end() {
        if(this.curState != RoomState.PLAY)
            throw new IllegalStateException("종료할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.END;
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
