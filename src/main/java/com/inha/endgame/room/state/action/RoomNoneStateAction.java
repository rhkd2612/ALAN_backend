package com.inha.endgame.room.state.action;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        Date now = new Date();
        if(room.getRoomUsers().isEmpty()) {
            Date closeTime = new Date(room.getCreateAt().getTime() + 60000);
            if (now.after(closeTime))
                room.setNextState(RoomState.END);
        }
    }

    @Override
    public void onExit(Room room) {
        log.info("room none exit");
    }
}
