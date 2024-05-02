package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.request.*;
import com.inha.endgame.dto.response.*;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.room.RoomUserCop;
import com.inha.endgame.user.MissionState;
import com.inha.endgame.user.StunState;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void onUpdateUserRequest(ClientEvent<UpdateUserRequest> event) {
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

    @EventListener
    public void onAimRequest(ClientEvent<AimRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            roomService.updateAim(roomId, request.getAimState(), request.getAimPos());
            unitySocketService.sendMessageRoom(roomId, new AimResponse(request.getAimPos(), request.getAimAt(), request.getAimState()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onStunRequest(ClientEvent<StunRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            RoomUserCop copUser;
            if(request.getStunState().equals(StunState.END))
                copUser = roomService.releaseStun(roomId);
            else
                copUser = roomService.stun(roomId, request.getTargetUsername());

            unitySocketService.sendMessageRoom(roomId, new StunResponse(copUser.getTargetUsername(), copUser.getShotAvailAt(), copUser.getStunAvailAt(), request.getStunState()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onShotRequest(ClientEvent<ShotRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            var targetUser = roomService.shot(roomId);
            var aliveUserCount = roomService.getAliveUserCount(roomId);
            RoomUserCop cop = roomService.getCop(roomId);

            unitySocketService.sendMessageRoom(roomId, new ShotResponse(targetUser.getUsername(), targetUser.getRoomUserType(), aliveUserCount, cop.getStunAvailAt(), targetUser.getCrimeType()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onSettingRoomRequest(ClientEvent<SettingRoomRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();
        int npcCount = request.getNpcCount();

        try {
            roomService.setNpc(roomId, npcCount);
            List<RoomUser> npcs = roomService.findAllRoomNpcsById(roomId);
            unitySocketService.sendMessageRoom(roomId, new SettingRoomResponse(npcs));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onPlayMissionRoomRequest(ClientEvent<PlayMissionRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            if(!request.getMissionState().equals(MissionState.FAIL))
                roomService.playMission(roomId, request.getUsername(), request.getMissionPhase(), request.getMissionPos(), request.getMissionState());
            unitySocketService.sendMessageRoom(roomId, new PlayMissionResponse(request));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}
