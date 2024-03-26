package com.inha.endgame.room.state.action;

import com.inha.endgame.room.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RoomNoneStateAction implements RoomStateAction {
    @Override
    public void onEnter(Room room) {
        log.info("room none enter");
    }

    @Override
    public void onUpdate(Room room) {
        // do nothing
    }

    @Override
    public void onExit(Room room) {
        log.info("room none exit");
    }
}
