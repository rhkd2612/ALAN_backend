package com.inha.endgame.room.thread;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.state.observer.StateChangePublisher;

import java.util.List;

public class RoomTask implements Runnable {
    private StateChangePublisher publisher;
    private List<Room> rooms;

    public RoomTask(StateChangePublisher publisher, List<Room> rooms) {
        this.publisher = publisher;
        this.rooms = rooms;
    }

    @Override
    public void run() {
        this.rooms.forEach(publisher::publishStateChange);
    }
}
