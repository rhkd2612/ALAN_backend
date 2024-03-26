package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.RoomState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomReadyStateAction implements RoomStateAction {
    private final RoomService roomService;
    private final UnitySocketService unitySocketService;

    @Override
    public void onEnter(Room room) {
        log.info("room ready enter");
        try {
            unitySocketService.sendMessageRoom(room.getRoomId(), new StartRoomResponse(RoomState.READY, room.getPlayAt()));
            roomService.selectJob(room.getRoomId());
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onUpdate(Room room) {
        if(room.getReadyAt() != null) {
            var now = new Date();
            if(now.after(room.getPlayAt())) {
                log.info("play room");
                roomService.playRoom(room.getRoomId());
            }
        }
    }

    @Override
    public void onExit(Room room) {
        log.info("room ready exit");
    }
}
