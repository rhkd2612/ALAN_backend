package com.inha.endgame.room.state.action;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomReadyStateAction implements RoomStateAction {
    private final RoomService roomService;

    @Override
    public void onEnter(Room room) {
        log.info("room ready enter");

        roomService.selectJob(room.getRoomId());
    }

    @Override
    public void onUpdate(Room room) {

    }

    @Override
    public void onExit(Room room) {
        log.info("room ready exit");
    }
}
