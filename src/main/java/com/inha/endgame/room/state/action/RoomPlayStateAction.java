package com.inha.endgame.room.state.action;

import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.EventInfoResponse;
import com.inha.endgame.dto.response.PlayRoomInfoResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUserNpc;
import com.inha.endgame.room.event.AnimEvent;
import com.inha.endgame.user.NpcState;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoomPlayStateAction implements RoomStateAction {
    private final UnitySocketService unitySocketService;

    @Override
    public void onEnter(Room room) {
        log.info("room play enter");
        try {
            room.setEvent(new AnimEvent());
            unitySocketService.sendMessageRoom(room.getRoomId(), new StartRoomResponse(RoomState.PLAY, room.getEndAt()));
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onUpdate(Room room) {
        // 변경 정보 전달
        try {
            var animEvent = room.getEvent();
            var now = new Date();

            var nextAnim = animEvent.getAnimNum();
            var nextAnimTimeAt = animEvent.getNextAnimAt().getTime();

            var animPlay = new AtomicBoolean(false);

            // 지났다면 애니메이션 실행
            if (now.after(new Date(nextAnimTimeAt))) {
                animPlay.set(true);
            } else if (!animEvent.isAnnounce() && now.after(new Date(nextAnimTimeAt - 5000))) {
                // 5초 전에 클라에 미리 알림
                animEvent.setAnnounce(true);
                unitySocketService.sendMessageRoom(room.getRoomId(), new EventInfoResponse(animEvent.getAnimNum(), animEvent.getNextAnimAt()));
            }

            room.getRoomNpcs().values().forEach(npc -> {
                if(npc instanceof RoomUserNpc) {
                    // npc 상태 변경 체크
                    RoomUserNpc roomUserNpc = (RoomUserNpc) npc;
                    if(roomUserNpc.getNpcState().equals(NpcState.DIE))
                        return;

                    if(roomUserNpc.isAnimPlay() && roomUserNpc.getStateUpAt().after(now))
                        return;

                    if(animPlay.get()) {
                        // 5초간 실행?
                        roomUserNpc.startAnim(nextAnim, new Date(nextAnimTimeAt + 5000));
                    } else {
                        // 상태 변경 시도
                        roomUserNpc.rollState();

                        // 이후 동작 실행
                        // TODO 추후 Anim 추가
                        if (roomUserNpc.getNpcState().equals(NpcState.MOVE)) {
                            // frameCount는 1초에 계산하는 횟수
                            roomUserNpc.setPos(roomUserNpc.getNextPos(10));
                        }
                    }
                }
            });

            unitySocketService.sendMessageRoom(room.getRoomId(), new PlayRoomInfoResponse(room.getAllMembers()));

            if(animPlay.get())
                animEvent.resetAnimEvent();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    @Override
    public void onExit(Room room) {

    }
}
