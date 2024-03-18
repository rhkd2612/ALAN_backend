package com.inha.endgame.room;

import com.inha.endgame.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final Map<Long, Room> mapRoom = new ConcurrentHashMap<>();

    public Collection<Room> getAllRoom() {
        return Collections.unmodifiableCollection(mapRoom.values());
    }

    @PostConstruct
    void init() {
        // TEST용
        Room room = new Room();
        mapRoom.put(room.getRoomId(), room);
    }

    public void joinRoom(long roomId, User user) {
        Room room = mapRoom.get(roomId);
        if(room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        room.join(user.toRoomUser());
    }
}
