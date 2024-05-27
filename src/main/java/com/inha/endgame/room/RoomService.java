package com.inha.endgame.room;

import com.inha.endgame.core.excel.JsonReader;
import com.inha.endgame.core.excel.MapReader;
import com.inha.endgame.dto.PlayRoomDto;
import com.inha.endgame.dto.ReconnectInfo;
import com.inha.endgame.dto.ReportInfo;
import com.inha.endgame.dto.UseItemInfo;
import com.inha.endgame.user.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService {
    private final Map<Long, Room> mapRoom = new ConcurrentHashMap<>();
    private final UserService userService;
    private final MapReader mapReader;

    public static float minX;
    public static float minZ;
    public static float maxX;
    public static float maxZ;

    private static int last_room_id = 0;

    public Room createRoom() {
        Room room = new Room(++last_room_id);
        mapRoom.put(room.getRoomId(), room);

        return room;
    }

    public Collection<Room> getAllRoom() {
        return Collections.unmodifiableCollection(mapRoom.values());
    }

    public Room findRoomById(long roomId) {
        return mapRoom.get(roomId);
    }

    public RoomUser findUserByUsername(long roomId, String username) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return room.getRoomUsers().get(username);
    }

    public List<RoomUser> findAllRoomUsersById(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return new ArrayList<>(room.getRoomUsers().values());
    }

    public List<RoomUser> findAllRoomNpcsById(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return new ArrayList<>(room.getRoomNpcs().values());
    }

    public List<RoomUser> findAllRoomUsersWithNpcById(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return new ArrayList<>(room.getAllMembers());
    }

    public String checkUser(long roomId, String username) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if (room.getRoomUsers().containsKey(username))
            return room.getRoomUsers().get(username).getNickname();

        return null;
    }

    public ReconnectInfo getReconnectInfo(long roomId, String reconnectUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if(room.getCurState().equals(RoomState.NONE))
            throw new IllegalStateException("진행 중이 아닌 방은 재연결 불가합니다.");

        ReconnectInfo result = new ReconnectInfo();
        result.setCurrentDateAt(new Date());

        RoomUser reconnectUser = this.findUserByUsername(roomId, reconnectUsername);
        if(reconnectUser.checkCrimeUser()) {
            RoomUserCrime crimeUser = (RoomUserCrime) reconnectUser;
            result.setRemainItemCount(crimeUser.getRemainItemCount());
            result.setMissionInfo(crimeUser.getMissionPos());
            result.setCurrentMissionPhase(crimeUser.getClearMissionPhase() + 1);

            List<String> targetInfo = null;
            if (reconnectUser.getCrimeType().equals(CrimeType.ASSASSIN)) {
                var assassin = (RoomUserCrimeAssassin)reconnectUser;
                targetInfo = assassin.getTargetInfo();
            }
            result.setTargetInfo(targetInfo);
        } else {
            // 경찰인 경우 현재 상태 강제 종료
            RoomUserCop reconnectCop = (RoomUserCop) reconnectUser;
            switch(reconnectCop.getCopAttackState()) {
                case AIM:
                    this.updateAim(roomId, AimState.END, new rVector3D(0,0,0));
                    break;
                case STUN:
                    this.releaseStun(roomId);
                    break;
                case SHOT:
                    reconnectCop.setCopAttackState(CopAttackState.NONE);
                    break;
            }
        }

        var recentUseItemInfo = room.getRecentUseItemInfo();
        var recentReportUserInfo = room.getRecentReportUserInfo();
        result.setPlayRoomDto(new PlayRoomDto(room, recentUseItemInfo, recentReportUserInfo));

        return result;
    }

    public int getAliveUserCount(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        List<RoomUser> aliveUser = room.getRoomUsers().values().stream()
                .filter(user -> user.getRoomUserType().equals(RoomUserType.USER))
                .filter(user -> !user.getUserState().equals(UserState.DIE))
                .collect(Collectors.toList());

        return aliveUser.size();
    }

    public RoomUserCop getCop(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        return room.getCop();
    }

    public synchronized void updateAim(long roomId, AimState aimState, rVector3D targetPos) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var copUser = room.getCop();
        if (aimState.equals(AimState.END))
            copUser.endAimingAndStun();
        else
            copUser.aiming(targetPos);
    }

    public synchronized RoomUserCop stun(long roomId, String targetUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var copUser = room.getCop();
        var targetUser = room.getAllMembersMap().get(targetUsername);

        copUser.stun(targetUser);
        targetUser.stunned();

        return copUser;
    }

    public synchronized RoomUserCop releaseStun(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var copUser = room.getCop();
        if (copUser.getTargetUsername() == null)
            return copUser;

        var targetUser = room.getAllMembersMap().get(copUser.getTargetUsername());
        if (!targetUser.getUserState().equals(UserState.DIE))
            targetUser.releaseStun();

        var nextStunCoolTime = JsonReader._time(JsonReader.model("shot", "shot_rule", "InspectCoolTime"));
        copUser.setStunAvailAt(new Date(new Date().getTime() + nextStunCoolTime));

        copUser.endAimingAndStun();

        return copUser;
    }

    public synchronized RoomUser shot(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var copUser = room.getCop();
        var targetUser = room.getAllMembersMap().get(copUser.getTargetUsername());

        copUser.checkShot(targetUser);

        targetUser.die();
        copUser.endAimingAndStun();

        if(!targetUser.checkCrimeUser())
            room.setTrueEnd(false);

        RoomUserCrimeAssassin assassin = room.getAssassin();
        if (assassin != null && assassin.getTargetUsernames().contains(copUser.getTargetUsername()))
            assassin.killTarget(copUser.getTargetUsername());

        var nextStunCoolTime = JsonReader._time(JsonReader.model("shot", "shot_rule", "InspectCoolTime"));
        copUser.setStunAvailAt(new Date(new Date().getTime() + nextStunCoolTime));

        return targetUser;
    }

    public synchronized void assassinKill(long roomId, String targetUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        RoomUserCrimeAssassin assassin = room.getAssassin();
        if (assassin == null)
            throw new IllegalArgumentException("암살자가 없습니다.");

        RoomUser targetNpc = room.getRoomNpcs().get(targetUsername);
        if (targetNpc == null)
            throw new IllegalArgumentException("NPC만 타겟으로 지정할 수 있습니다.");

        if (!assassin.getTargetUsernames().contains(targetUsername))
            throw new IllegalArgumentException("타겟이 아닙니다.");

        targetNpc.die();
        assassin.killTarget(targetUsername);

        room.setTrueEnd(false);
    }

    public ReportInfo reportUser(long roomId, String reportUsername, String targetUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var result = new ReportInfo();

        RoomUser reportUser = room.getRoomUsers().get(reportUsername);
        Date nextReportAvailAt = reportUser.report();

        result.setReportUsername(reportUsername);
        result.setNextReportAvailAt(nextReportAvailAt);

        var reportStartAt = JsonReader._time(JsonReader.model("report", "report_rule", "EffectActivatingTime"));
        var reportEndAt = JsonReader._time(JsonReader.model("report", "report_rule", "EffectContinuousTime"));
        result.setHighlightStartAt(new Date(new Date().getTime() + reportStartAt));
        result.setHighlightEndAt(new Date(new Date().getTime() + reportEndAt));

        // npc를 지목하면 자기 자신에게 패널티가 적용된다
        if(room.getRoomUsers().containsKey(targetUsername)) {
            result.setTargetUsername(targetUsername);
            result.setReportMessage("다른 범죄자에게 신고 당하여 경찰에게 위치가 공유 됩니다.");
        }
        else {
            result.setTargetUsername(reportUsername);
            result.setReportMessage("NPC를 허위 신고하여 경찰에게 위치가 공유 됩니다.");
        }

        room.recordReportUser(result);

        return result;
    }


    public void joinRoom(long roomId, User user) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        room.join(user.toRoomUser());
        userService.syncRoom(user);
    }

    public Map<String, String> leaveRoom(long roomId, String leaveUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if(!room.getCurState().equals(RoomState.NONE))
            throw new IllegalArgumentException("현재는 종료할 수 없는 방 입니다.");

        Map<String, String> sessionIds = new HashMap();
        String hostUsername = room.getHostUsername();

        // 방장이 나가는 경우 모두 퇴장 처리
        if(hostUsername.equals(leaveUsername)) {
            for(var username : room.getRoomUsers().keySet()) {
                try {
                    sessionIds.put(userService.leaveRoom(roomId, username), username);
                    room.kick(username);
                } catch(Exception e) {}
            }

            mapRoom.remove(room.getRoomId(), room);
        } else {
            sessionIds.put(userService.leaveRoom(roomId, leaveUsername), leaveUsername);
            room.kick(leaveUsername);
        }

        return sessionIds;
    }

    public void startRoom(long roomId, String startUserUsername) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if(room.getRoomUsers().size() <= 1)
            throw new IllegalStateException("2인 이상이여야 시작할 수 있습니다.");

        if(!room.getHostUsername().equals(startUserUsername))
            throw new IllegalArgumentException("방장만 시작할 수 있습니다.");

        room.start();
    }

    public void setNpc(long roomId, int npcCount) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if (npcCount == 0)
            throw new IllegalArgumentException("1이상의 npc 수가 필요합니다.");

        List<rVector3D> randomNpcPos = mapReader.getRandomNpcPos(npcCount);
        room.setRoomNpc(npcCount, randomNpcPos);
    }

    public void playMission(long roomId, String username, int missionPhase, MissionState missionState) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var crime = (RoomUserCrime) room.getRoomUsers().get(username);
        if (missionState.equals(MissionState.CLEAR))
            crime.clearMission(missionPhase);
    }

    public void useItem(long roomId, String username, rVector3D useItemPos) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if (!room.getCurState().equals(RoomState.PLAY))
            throw new IllegalArgumentException("방이 진행 중이지 않습니다.");

        var crime = (RoomUserCrime) room.getRoomUsers().get(username);
        crime.useItem();

        room.recordUseItem(new UseItemInfo(new Date(), useItemPos, username));
    }

    public void updateUser(long roomId, RoomUser roomUser) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        if (!room.getCurState().equals(RoomState.PLAY))
            throw new IllegalArgumentException("방이 진행 중이지 않습니다.");

        var username = roomUser.getUsername();
        if (room.getRoomUsers().containsKey(username)) {
            var prevUser = room.getRoomUsers().get(username);
            prevUser.updateUser(roomUser);
        }
    }

    public void playRoom(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");
        room.play();
    }

    public void selectJob(long roomId) {
        Room room = mapRoom.get(roomId);
        if (room == null)
            throw new IllegalArgumentException("참여할 수 없는 방입니다.");

        var roomUsers = new ArrayList<>(room.getRoomUsers().values());
        var cop = roomUsers.stream().filter(roomUser -> roomUser.getUsername().equals(room.getHostUsername())).findAny().orElseThrow();
        var copUsername = cop.getUsername();
        cop.beCop();

        roomUsers.forEach(roomUser -> {
            if (!roomUser.getUsername().equals(copUsername)) {
                CrimeType randomCrimeType = room.getRandomCrimeType();
                roomUser.beCrime(randomCrimeType);

                RoomUserCrime crimeUser = RoomUserCrime.createCrime(roomUser, randomCrimeType);

                if (randomCrimeType == CrimeType.ASSASSIN) {
                    int targetCount = 3;
                    room.getRoomNpcs().keySet().forEach(npc -> {
                        RoomUserCrimeAssassin assassin = (RoomUserCrimeAssassin) crimeUser;
                        if (assassin.getTargetUsernames().size() >= targetCount)
                            return;
                        assassin.addTarget(npc);
                    });
                }

                switch (randomCrimeType) {
                    case SPY:
                        room.setSpyUsername(roomUser.getUsername());
                        break;
                    case BOOMER:
                        room.setBoomerUsername(roomUser.getUsername());
                        break;
                    case ASSASSIN:
                        room.setAssassinUsername(roomUser.getUsername());

                        int targetCount = 3;
                        room.getRoomNpcs().keySet().forEach(npc -> {
                            RoomUserCrimeAssassin assassin = (RoomUserCrimeAssassin) crimeUser;
                            if (assassin.getTargetUsernames().size() >= targetCount)
                                return;
                            assassin.addTarget(npc);
                        });
                        break;
                }

                room.getRoomUsers().put(roomUser.getUsername(), crimeUser);
            }
        });

        room.setCopUsername(copUsername);
        room.getRoomUsers().put(copUsername, new RoomUserCop(cop));
    }
}
