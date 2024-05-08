package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.dto.RoomDto;
import com.inha.endgame.dto.request.*;
import com.inha.endgame.dto.response.*;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("UserEventListener");

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
                result.add(new RoomDto(room));
            });

            unitySocketService.sendMessage(session, new RoomListResponse(result));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

    @EventListener
    public void onAddUserRequest(ClientEvent<AddUserRequest> event) throws IOException, NoSuchAlgorithmException {
        var session = event.getSession();
        var request = event.getClientRequest();
        var roomId = request.getRoomId();
        var newUser = userService.addUser(session, request.getUsername(), request.getNickname());

        try {
            session.sendMessage(new TextMessage(newUser.getUsername()));

            if(newUser.isNew()) {
                roomService.joinRoom(roomId, newUser);

                var roomUsers = roomService.findAllRoomUsersById(roomId);
                unitySocketService.sendMessageRoom(roomId, new AddUserResponse(newUser.getNickname(), roomUsers));

                // 새로 들어오면 방 정보 다시 세팅
                List<RoomUser> npcs = roomService.findAllRoomNpcsById(roomId);
                unitySocketService.sendMessage(session, new SettingRoomResponse(npcs));
            }
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
            roomService.exitRoom(roomId, newUser);
            session.close();
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
    public void pingRequest(ClientEvent<PingRequest> event) {
        var session = event.getSession();

        try {
            unitySocketService.sendMessage(session, new PingResponse(new Date()));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }

}
