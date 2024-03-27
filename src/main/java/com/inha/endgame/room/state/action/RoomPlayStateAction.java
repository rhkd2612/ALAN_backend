package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.PlayRoomInfoResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUserNpc;
import com.inha.endgame.user.NpcState;
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
        room.getRoomNpcs().values().forEach(npc -> {
            if(npc instanceof RoomUserNpc) {
                // npc 상태 변경 체크
                RoomUserNpc roomUserNpc = (RoomUserNpc) npc;
                if(roomUserNpc.getNpcState().equals(NpcState.DIE))
                    return;

                // 상태 변경 시도
                roomUserNpc.rollState();

                // 이후 동작 실행
                // TODO 추후 Anim 추가
                if(roomUserNpc.getNpcState().equals(NpcState.MOVE)) {
                    // frameCount는 1초에 계산하는 횟수
                    roomUserNpc.setPos(roomUserNpc.getNextPos(10));
                }
            }
        });

        // 변경 정보 전달
        try {
            unitySocketService.sendMessageRoom(room.getRoomId(), new PlayRoomInfoResponse(room.getAllMembers()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onExit(Room room) {

    }
}
