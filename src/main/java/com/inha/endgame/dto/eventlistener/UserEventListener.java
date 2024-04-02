package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.dto.request.CheckUserRequest;
import com.inha.endgame.dto.request.StartRoomRequest;
import com.inha.endgame.dto.response.AddUserResponse;
import com.inha.endgame.dto.response.CheckUserResponse;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("UserEventListener");

    private final UnitySocketService unitySocketService;
    private final UserService userService;
    private final RoomService roomService;

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
            boolean isExist = roomService.checkUser(roomId, request.getUsername());
            unitySocketService.sendMessage(session, new CheckUserResponse(isExist));
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}
