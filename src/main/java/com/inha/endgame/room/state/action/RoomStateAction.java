package com.inha.endgame.room.state.action;

import com.inha.endgame.room.Room;

public interface RoomStateAction {
    void onEnter(Room room);
    void onUpdate(Room room);
    void onExit(Room room);
}
