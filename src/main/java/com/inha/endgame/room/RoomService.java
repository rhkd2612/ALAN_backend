package com.inha.endgame.room;

import com.inha.endgame.dto.request.CheckUserRequest;
import com.inha.endgame.user.User;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Map<Long, Room> mapRoom = new ConcurrentHashMap<>();
    private final UserService userService;

    public Collection<Room> getAllRoom() {
        return Collections.unmodifiableCollection(mapRoom.values());
    }

    @PostConstruct
    void init() {
        // TEST용
        for(int i = 0; i < 5; i++) {
            Room room = new Room(i);
            mapRoom.put(room.getRoomId(), room);
        }
    }

    public Room findRoomById(long roomId) {
        return mapRoom.get(roomId);
    }

    public List<RoomUser> findAllRoomUsersById(long roomId) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return new ArrayList<>(room.getRoomUsers().values());
    }

    public List<RoomUser> findAllRoomUsersWithNpcById(long roomId) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return new ArrayList<>(room.getAllMembers());
    }

    public boolean checkUser(long roomId, String username) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return room.getRoomUsers().containsKey(username);
    }

    public void joinRoom(long roomId, User user) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        room.join(user.toRoomUser());
        userService.syncRoom(user);
    }

    public void startRoom(long roomId) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        // TODO 나중에 추가되어야 함 귀찮으니 일단 주석..
        // if(room.getRoomUsers().size() < 1)
        //    throw new IllegalStateException("1인 이상이여야 시작할 수 있습니다.");

        room.start();
    }

    public List<RoomUser> setNpc(long roomId, int npcCount, float npcMinX, float npcMaxX, float npcMinZ, float npcMaxZ) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if(npcCount == 0)
            throw new IllegalArgumentException("1이상의 npc 수가 필요합니다.");

        return room.setRoomNpc(npcCount, npcMinX, npcMaxX, npcMinZ, npcMaxZ);
    }

    public void updateUser(long roomId, RoomUser roomUser) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if(!room.getCurState().equals(RoomState.PLAY))
            throw new IllegalArgumentException("방이 진행 중이지 않습니다.");

        if(room.getRoomUsers().containsKey(roomUser.getUsername())) {
            room.getRoomUsers().put(roomUser.getUsername(), roomUser);
        }
    }

    public void playRoom(long roomId) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");
        room.play();
    }
    
    public void selectJob(long roomId) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        // TODO  첫 MVP에는 보스가 없다
    }

    public void exitRoom(long roomId, User user) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        room.kick(user.toRoomUser());
        userService.logout(user);
    }
}
