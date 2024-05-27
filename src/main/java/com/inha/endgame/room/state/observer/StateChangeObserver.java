package com.inha.endgame.room.state.observer;

import com.inha.endgame.room.state.action.RoomEndStateAction;
import com.inha.endgame.room.state.action.RoomNoneStateAction;
import com.inha.endgame.room.state.action.RoomPlayStateAction;
import com.inha.endgame.room.state.action.RoomReadyStateAction;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class StateChangeObserver implements StateChangeListener {
    private final RoomNoneStateAction noneAction;
    private final RoomReadyStateAction readyAction;
    private final RoomPlayStateAction playAction;
    private final RoomEndStateAction endAction;

    @Override
    @EventListener
    public void onStateChange(StateChangeEvent event) {
        var room = event.getRoom();
        var curState = room.getCurState();
        var nextState = room.getNextState();

        if (nextState == null || curState != null && curState == nextState || room.getNextStateAt() != null && room.getNextStateAt().after(new Date())) {
            // 다음 상태가 없다면 Update
            switch (curState) {
                case NONE: noneAction.onUpdate(room); break;
                case READY: readyAction.onUpdate(room); break;
                case PLAY: playAction.onUpdate(room); break;
                case END: endAction.onUpdate(room); break;
            }
            return;
        }

        // 다음 상태가 있다면 change, 첫 init시 cur이 없을 수 있음
        if (curState != null) {
            switch (curState) {
                case NONE: noneAction.onExit(room); break;
                case READY: readyAction.onExit(room); break;
                case PLAY: playAction.onExit(room); break;
                case END: endAction.onExit(room); break;
            }
        }

        switch (nextState) {
            case NONE: noneAction.onEnter(room); break;
            case READY: readyAction.onEnter(room); break;
            case PLAY: playAction.onEnter(room); break;
            case END: endAction.onEnter(room); break;
        }

        room.setCurState(nextState);
        room.setNextState(null);
        room.setNextStateAt(null);
    }
}