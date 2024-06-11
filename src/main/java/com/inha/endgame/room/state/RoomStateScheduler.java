package com.inha.endgame.room.state;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomService;
import com.inha.endgame.room.state.observer.StateChangePublisher;
import com.inha.endgame.room.thread.RoomTask;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class RoomStateScheduler {
    private final RoomService roomService;
    private final StateChangePublisher publisher;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final int ROOM_SIZE_PER_THREAD = 10;

    // 0.5초마다 상태 체크(나중에는 더 빨리 바꿔야함, NPC는 별개로?)
    @Scheduled(fixedRate = 100)
    public void enterFrame() {
        // 10개 이하면 단일 쓰레드로 처리
        //if(roomService.getAllRoom().size() < ROOM_SIZE_PER_THREAD) {
            roomService.getAllRoom().forEach(publisher::publishStateChange);
            return;
        //}

//        var count = 0;
//        List<Room> taskRoomList = new ArrayList<>();
//        for(var room : roomService.getAllRoom()) {
//            if(room.isStateChangeProgress())
//                continue;
//
//            room.setStateChangeProgress(true);
//
//            count++;
//            taskRoomList.add(room);
//
//            if(count % ROOM_SIZE_PER_THREAD == 0) {
//                threadPoolTaskExecutor.execute(new RoomTask(publisher, new CopyOnWriteArrayList<>(taskRoomList)));
//                taskRoomList.clear();
//            }
//        }
//
//        // 잔여 룸 실행할 쓰레드 지정
//        if(!taskRoomList.isEmpty()) {
//            threadPoolTaskExecutor.execute(new RoomTask(publisher, new CopyOnWriteArrayList<>(taskRoomList)));
//            taskRoomList.clear();
//        }
    }
}
