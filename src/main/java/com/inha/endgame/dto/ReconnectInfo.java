package com.inha.endgame.dto;

import com.inha.endgame.room.rVector3D;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@NoArgsConstructor
public class ReconnectInfo {
    // 현재 서버 시간
    private Date currentDateAt;
    // 남은 아이템 개수
    private int remainItemCount;
    // 미션 정보
    private Map<Integer, rVector3D> missionInfo;
    // 현재 미션 페이지
    private int currentMissionPhase;
    // 방 정보
    private PlayRoomDto playRoomDto;
}
