package com.inha.endgame.room.state.observer;

import com.inha.endgame.room.Room;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StateChangeEvent extends ApplicationEvent {
    private final Room room;

    public StateChangeEvent(Object source, Room room) {
        super(source);
        this.room = room;
    }
}
