package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.SessionService;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.GameOverResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.user.User;
import com.inha.endgame.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomEndStateAction implements RoomStateAction {
    private final UnitySocketService unitySocketService;
    private final UserService userService;
    private final SessionService sessionService;

    @Override
    public void onEnter(Room room) {
        try {
            // FIXME 게임 엔드 추가
            unitySocketService.sendMessageRoom(room.getRoomId(), new StartRoomResponse(RoomState.END, null));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onUpdate(Room room) {
        long roomId = room.getRoomId();
        if(userService.getAllUser(roomId).isEmpty())
            return;


        Date now = new Date();
        room.getRoomUsers().keySet().forEach(username -> {
            User user = userService.getUser(roomId, username);
            if(user == null)
                return;

            // 유실을 방지하기 위해 3초에 한 번 재발송 함 -> 재발송 후 종료 확인 시 세션 킥
            if(user.getLastOverPacketSendAt() != null && now.before(new Date(user.getLastOverPacketSendAt().getTime() + 3000)))
                return;

            WebSocketSession userSession = sessionService.findSessionBySessionId(user.getSessionId());
            try {
                user.setLastOverPacketSendAt(now);
                unitySocketService.sendMessage(userSession, new GameOverResponse(room.getGameOverInfo()));
            } catch (Exception e) {
                userService.logout(roomId, user);
            }
        });
    }

    @Override
    public void onExit(Room room) {

    }
}
