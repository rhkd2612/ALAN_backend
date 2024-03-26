package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.PlayRoomInfoResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomPlayStateAction implements RoomStateAction {
    private final UnitySocketService unitySocketService;

    @Override
    public void onEnter(Room room) {
        log.info("room play enter");
        try {
            unitySocketService.sendMessageRoom(room.getRoomId(), new StartRoomResponse(RoomState.PLAY, room.getEndAt()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onUpdate(Room room) {
        try {
            unitySocketService.sendMessageRoom(room.getRoomId(), new PlayRoomInfoResponse(room.getAllUserWithNpc()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onExit(Room room) {

    }
}
