package com.inha.endgame.eventlistener;

import com.inha.endgame.core.ClientEvent;
import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.unitysocket.UnitySocketService;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

@Service
@RequiredArgsConstructor
public class UserEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("UserEventListener");

    private final UnitySocketService unitySocketService;
    private final UserService userService;
    private final RoomService roomService;

    @EventListener
    public void onAddUserRequest(ClientEvent<AddUserRequest> event) {
        var session = event.getSession();
        try {
            var request = event.getClientRequest();
            var newUser = userService.addUser(session, request.getUserName());
            session.sendMessage(new TextMessage(newUser.getUserId()));

            roomService.joinRoom(request.getRoomId(), newUser);
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}
