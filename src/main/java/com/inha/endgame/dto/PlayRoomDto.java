package com.inha.endgame.dto;

import com.inha.endgame.room.Room;
import com.inha.endgame.room.RoomState;
import com.inha.endgame.room.RoomUser;
import com.inha.endgame.room.rVector3D;
import com.inha.endgame.user.UserState;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PlayRoomDto {
    private long roomId;
    private List<RoomUser> roomUsers;
    private int remainCrimeCount;
    private RoomState roomState;
    private Date roomEndAt;
    private String copUsername;
    private String spyUsername;
    private String boomerUsername;
    private String assassinUsername;

    // 최근 10초간 아이템 사용 위치
    private List<rVector3D> recentItemUseInfo;
    // 아직 만료되지 않은 report 정보
    private List<ReportInfo> recentReportInfo;

    public PlayRoomDto(Room room, List<rVector3D> recentItemUseInfo, List<ReportInfo> recentReportInfo) {
        this.roomId = room.getRoomId();
        this.remainCrimeCount = (int) room.getRoomUsers().values().stream()
                .filter(roomUser -> roomUser.checkCrimeUser() && !roomUser.getUserState().equals(UserState.DIE)).count();
        this.roomState = room.getCurState();
        this.roomEndAt = room.getEndAt();

        this.copUsername = room.getCopUsername();
        this.spyUsername = room.getSpyUsername();
        this.boomerUsername = room.getBoomerUsername();
        this.assassinUsername = room.getAssassinUsername();

        this.roomUsers = new ArrayList<>(room.getAllMembers());

        this.recentItemUseInfo = recentItemUseInfo;
        this.recentReportInfo = recentReportInfo;
    }
}
