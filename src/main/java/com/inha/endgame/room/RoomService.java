package com.inha.endgame.room;

import com.inha.endgame.user.User;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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
        Room room = new Room(1L);
        mapRoom.put(room.getRoomId(), room);
    }

    public Room findRoomById(long roomId) {
        return mapRoom.get(roomId);
    }

    public List<RoomUser> findAllRoomUsersById(long roomId) {
        Room room = mapRoom.get(roomId);
        return new CopyOnWriteArrayList<>(room.getRoomUsers().values());
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
