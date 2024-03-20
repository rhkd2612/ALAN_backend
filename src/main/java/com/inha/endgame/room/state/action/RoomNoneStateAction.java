package com.inha.endgame.room.state.action;

import com.inha.endgame.room.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomNoneStateAction implements RoomStateAction {
    @Override
    public void onEnter(Room room) {
        // room init
    }

    @Override
    public void onUpdate(Room room) {
        // do nothing
    }

    @Override
    public void onExit(Room room) {
        // do nothing
    }
}
