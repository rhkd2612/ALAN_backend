package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.RoomDto;
import com.inha.endgame.dto.request.*;
import com.inha.endgame.dto.response.*;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.user.User;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private final UnitySocketService unitySocketService;
    private final UserService userService;
    private final RoomService roomService;

    @EventListener
    public void onCreateRoomRequest(ClientEvent<CreateRoomRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();

        try {
            Room newRoom = roomService.createRoom();
            unitySocketService.sendMessage(session, new CreateRoomResponse(newRoom.getRoomId()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onRoomListRequest(ClientEvent<RoomListRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();

        try {
            List<RoomDto> result = new ArrayList<>();
            roomService.getAllRoom().forEach(room -> {
                if(room.getHostNickname() != null)
                    result.add(new RoomDto(room));
            });

            unitySocketService.sendMessage(session, new RoomListResponse(result));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onAddUserRequest(ClientEvent<AddUserRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();
        var newUser = userService.addUser(session, roomId, request.getUsername(), request.getNickname());

        try {
            session.sendMessage(new TextMessage(newUser.getUsername()));

            if(newUser.isNew()) {
                roomService.joinRoom(roomId, newUser);
            }

            var roomUsers = roomService.findAllRoomUsersByRoomIdOrderByCop(roomId);
            unitySocketService.sendMessageRoom(roomId, new AddUserResponse(newUser.getNickname(), roomUsers));

            // 새로 들어오면 방 정보 다시 세팅(방장 제외)
            if(roomUsers.size() > 1) {
                List<RoomUser> npcs = roomService.findAllRoomNpcsById(roomId);
                unitySocketService.sendMessage(session, new SettingRoomResponse(npcs));
            }
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);

            if(!newUser.isNew())
                roomService.leaveRoom(roomId, newUser.getUsername());
        }
    }

    @EventListener
    public void onCheckUserRequest(ClientEvent<CheckUserRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            String prevNickname = roomService.checkUser(roomId, request.getUsername());
            unitySocketService.sendMessage(session, new CheckUserResponse(prevNickname != null, prevNickname));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onLeaveRoomRequest(ClientEvent<LeaveRoomRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        try {
            Map<String, String> leaveUserMap = roomService.leaveRoom(roomId, request.getLeaveUsername());
            var leaveUsername = new ArrayList<>(leaveUserMap.values());

            // 방을 나갔으므로 세션 기반으로 전송
            for (String sessionId : leaveUserMap.keySet()) {
                unitySocketService.sendMessage(sessionId, new LeaveRoomResponse(leaveUsername, true));
            }

            // 단일 leave는 방 유저들에게는 유저 정보 기반으로 전송 / 방장이 나간 경우에는 방이 사라지므로 보내지 않음
            if(leaveUserMap.size() == 1) {
                unitySocketService.sendMessageRoom(roomId, new LeaveRoomResponse(leaveUsername, false));
            }
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void pingRequest(ClientEvent<PingRequest> event) {
        var session = event.getSession();

        try {
            unitySocketService.sendMessage(session, new PingResponse(new Date()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onReconnectRequest(ClientEvent<ReconnectRequest> event) {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        userService.reconnect(session, roomId, request.getUsername());

        try {
            var reconnectUser = roomService.findUserByUsername(roomId, request.getUsername());
            var reconnectInfo = roomService.getReconnectInfo(roomId, request.getUsername());
            unitySocketService.sendMessage(session, new ReconnectResponse(reconnectUser, reconnectInfo));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    /**
     *  게임 종료 시 GameOverResponse -> GameOverConfirmRequest(TimeWait) -> confirm이 오지 않을 시 EndUpdate에서 재전송
     */
    @EventListener
    public void onGameOverConfirmRequest(ClientEvent<GameOverConfirmRequest> event) {
        var request = event.getClientRequest();
        var roomId = request.getRoomId();

        User user = userService.getUser(roomId, request.getUsername());
        userService.logout(roomId, user);
    }
}
