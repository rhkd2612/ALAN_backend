package com.inha.endgame.room.state;

import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.state.observer.StateChangePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomStateScheduler {
    private final RoomService roomService;
    private final StateChangePublisher publisher;

    // 0.5초마다 상태 체크(나중에는 더 빨리 바꿔야함, NPC는 별개로?)
    @Scheduled(fixedRate = 500)
    public void enterFrame() {
        roomService.getAllRoom().forEach(publisher::publishStateChange);
    }
}
