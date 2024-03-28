package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.request.UpdateUserRequest;
import com.inha.endgame.dto.request.StartRoomRequest;
import com.inha.endgame.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("RoomEventListener");

    private final UnitySocketService unitySocketService;
    private final RoomService roomService;

    @EventListener
    public void onStartRoomRequest(ClientEvent<StartRoomRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            roomService.startRoom(roomId);
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onChangeUserRequest(ClientEvent<UpdateUserRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();
        var roomUser = request.getRoomUser();

        try {
            roomService.updateUser(roomId, roomUser);
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}