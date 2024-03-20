package com.inha.endgame.room.state.observer;

import com.inha.endgame.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StateChangePublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishStateChange(Room room) {
        var changeEvent = new StateChangeEvent(this, room);
        eventPublisher.publishEvent(changeEvent);
    }
}
