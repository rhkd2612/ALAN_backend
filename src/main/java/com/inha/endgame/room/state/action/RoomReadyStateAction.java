package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.SessionService;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.SelectJobResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUserNpc;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomReadyStateAction implements RoomStateAction {
    private final RoomService roomService;
    private final SessionService sessionService;
    private final UserService userService;
    private final UnitySocketService unitySocketService;

    @Override
    public void onEnter(Room room) {
        log.info("room ready enter");
        try {
            unitySocketService.sendMessageRoom(room.getRoomId(), new StartRoomResponse(RoomState.READY, room.getPlayAt()));
            roomService.selectJob(room.getRoomId());

            userService.getAllUser().forEach(user -> {
                var userSession = sessionService.findSessionBySessionId(user.getSessionId());
                var roomUserType = room.getRoomUsers().get(user.getUsername()).getRoomUserType();
                try {
                    unitySocketService.sendMessage(userSession, new SelectJobResponse(roomUserType));
                } catch (Exception e) {}
            });
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onUpdate(Room room) {
        if(room.getReadyAt() != null) {
            var now = new Date();
            if(now.after(room.getPlayAt())) {
                log.info("play room");
                roomService.playRoom(room.getRoomId());
            }
        }
    }

    @Override
    public void onExit(Room room) {
        log.info("room ready exit");
        room.getRoomNpcs().values().forEach(npc -> {
            if(npc instanceof RoomUserNpc) {
                RoomUserNpc roomUserNpc = (RoomUserNpc) npc;
                roomUserNpc.startNpc();
            }
        });
    }
}
