package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.SessionService;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.SelectJobResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.*;
import com.inha.endgame.user.CrimeType;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
            var roomUsers = roomService.findAllRoomUsersById(room.getRoomId());

            userService.getAllUser().forEach(user -> {
                var userSession = sessionService.findSessionBySessionId(user.getSessionId());
                var roomUser = room.getRoomUsers().get(user.getUsername());
                try {
                    Map<Integer, rVector3D> missionInfo = null;
                    List<String> targetInfo = null;

                    if(roomUser.checkCrimeUser()) {
                        var crime = (RoomUserCrime)roomUser;
                        missionInfo = crime.getMissionPos();

                        if (roomUser.getCrimeType().equals(CrimeType.ASSASSIN)) {
                            var assassin = (RoomUserCrimeAssassin) roomUser;
                            targetInfo = assassin.getTargetInfo();
                        }
                    }

                    unitySocketService.sendMessage(userSession, new SelectJobResponse(roomUser.getRoomUserType(), roomUser.getPos(), roomUser.getCrimeType(), roomUsers, missionInfo, targetInfo));
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
