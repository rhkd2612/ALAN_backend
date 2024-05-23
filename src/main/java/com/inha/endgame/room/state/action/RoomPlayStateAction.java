package com.inha.endgame.room.state.action;

import com.inha.endgame.core.excel.MapReader;
import com.inha.endgame.dto.GameOverInfo;
import com.inha.endgame.user.CopAttackState;
import com.inha.endgame.user.UserState;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import com.inha.endgame.dto.response.EventInfoResponse;
import com.inha.endgame.dto.response.PlayRoomInfoResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.room.*;
import com.inha.endgame.room.event.AnimEvent;
import com.inha.endgame.user.NpcState;
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
    private final MapReader mapReader;
    private final RoomService roomService;

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
            if (isGameOver(room))
                return;

            var animEvent = room.getEvent();
            var now = new Date();

            var nextAnim = animEvent.getAnimNum();
            var nextAnimTimeAt = animEvent.getNextAnimAt().getTime();

            var animPlay = new AtomicBoolean(false);

            // 경찰 먼저 실행
            RoomUserCop cop = room.getCop();
            switch(cop.getCopAttackState()) {
                case AIM: {
                    break;
                }
                case STUN: {
                    boolean isStun = room.getAllMembers().stream().anyMatch(member -> member.getUserState().equals(UserState.STUN));
                    if(!isStun)
                        cop.setCopAttackState(CopAttackState.NONE);
                    break;
                }
                case NONE: {
                    break;
                }
            }

            if(cop.getTargetUsername() == null)
                room.getAllMembers().forEach(RoomUser::releaseStun);

            // 지났다면 애니메이션 실행
            if (now.after(new Date(nextAnimTimeAt))) {
                animPlay.set(true);
            } else if (!animEvent.isAnnounce() && now.after(new Date(nextAnimTimeAt - 5000))) {
                // 5초 전에 클라에 미리 알림
                animEvent.setAnnounce(true);
                unitySocketService.sendMessageRoom(room.getRoomId(), new EventInfoResponse(animEvent.getAnimNum(), animEvent.getNextAnimAt()));
            }

            room.getRoomNpcs().values().forEach(npc -> {
                if (npc instanceof RoomUserNpc) {
                    // npc 상태 변경 체크
                    RoomUserNpc roomUserNpc = (RoomUserNpc) npc;
                    if (roomUserNpc.getUserState().equals(UserState.DIE))
                        return;

                    if (roomUserNpc.isAnimPlay() && roomUserNpc.getStateUpAt().after(now))
                        return;

                    if (animPlay.get()) {
                        // 5초간 실행?
                        roomUserNpc.startAnim(nextAnim, new Date(nextAnimTimeAt + 5000));
                    } else {
                        // 상태 변경 시도
                        roomUserNpc.rollState(false);

                        // 이후 동작 실행
                        // TODO 추후 Anim 추가
                        if (roomUserNpc.getNpcState().equals(NpcState.MOVE)) {
                            // frameCount는 1초에 계산하는 횟수
                            rVector3D nextPos = roomUserNpc.getNextPos(10);
                            rVector3D predictPos = roomUserNpc.getNextPos(1);
                            if(mapReader.check(nextPos, 2) && mapReader.check(nextPos, predictPos))
                                roomUserNpc.setPos(nextPos);
                            else {
                                roomUserNpc.rollState(true);
                            }
                        }
                    }
                }
            });

            RoomUserCrimeBoomer boomer = room.getBoomer();
            Date boomAt = null;
            if(boomer != null)
                boomAt = boomer.getBoomAt();

            unitySocketService.sendMessageRoom(room.getRoomId(), new PlayRoomInfoResponse(room.getAllMembers(), room.getEndAt(), boomAt));

            if(animPlay.get())
                animEvent.resetAnimEvent();
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private boolean isGameOver(Room room) {
        GameOverInfo gameOverInfo = calculateGameOverInfo(room);
        if(gameOverInfo != null) {
            room.setGameOverInfo(gameOverInfo);
            room.setNextState(RoomState.END);
            return true;
        }

        return false;
    }

    private GameOverInfo calculateGameOverInfo(Room room) {
        GameOverInfo gameOverInfo = null;
        Date now = new Date();

        // 1. 경찰 승리 체크
        // 1-1. 안전한? 거리(시간 초과)
        RoomUserCop cop = roomService.getCop(room.getRoomId());
        if(room.getEndAt().after(now)) {
            gameOverInfo = new GameOverInfo(GameOverInfo.OverJob.COP, cop.getUsername(), cop.getNickname(), cop.getKillUserAt());
            gameOverInfo.putDetail(GameOverInfo.OverType.NORMAL, "안전한? 거리", "그들도 시민 속에 살아가고 있습니다. 곧 잡히겠죠..?", "범죄가 일어나지 않음");
            return gameOverInfo;
        }

        // 1-2. 범죄자 모두 처치
        int aliveUserCount = roomService.getAliveUserCount(room.getRoomId());
        if(aliveUserCount == 0) {
            gameOverInfo = new GameOverInfo(GameOverInfo.OverJob.COP, cop.getUsername(), cop.getNickname(), cop.getKillUserAt());

            // 1-2-1 저는 커서 경찰 아저씨가 될거에요(npc 0명 이내 처치 승리)
            if(room.isTrueEnd()) {
                gameOverInfo.putDetail(GameOverInfo.OverType.TRUE, "매의 눈", "직업을 바꿔도 될 것 같은데요?", "시민 전원 생존");
            } else {
                int npcKillCount = cop.getNpcKillCount();
                if(npcKillCount > 5) {
                    // 1-2-2 부정경찰(5명 초과한 소시민 처치 승리)
                    gameOverInfo.putDetail(GameOverInfo.OverType.BAD, "부정경찰", "가끔 오해할 수도 있잖아요?", "5명을 초과한 시민 사살 후 승리");
                } else {
                    // 1-2-3 안전한 거리(npc 5명 이내 처치 승리)
                    gameOverInfo.putDetail(GameOverInfo.OverType.NORMAL, "안전한 거리", "행복을 위해 소수의 희생은 괜찮지 않을까요?", "5명 이하의 시민 사살 후 승리");
                }
            }

            return gameOverInfo;
        }


        // 2. 부머 승리 체크
        var boomer = room.getBoomer();
        if (boomer != null) {
            if (boomer.getBoomAt().after(now)) {
                gameOverInfo = new GameOverInfo(GameOverInfo.OverJob.BOOMER, boomer.getUsername(), boomer.getNickname(), boomer.getMissionClearAt());
                gameOverInfo.putDetail(GameOverInfo.OverType.BAD, "폭죽 놀이", "여러분의 세금이 하늘에서 터지고 있어요~", "경찰서 파괴");
                return gameOverInfo;
            }
        }

        // 3. 어쌔신 승리 체크
        var assassin = room.getAssassin();
        if (assassin != null) {
            if(assassin.getTargetUsernames().isEmpty()) {
                gameOverInfo = new GameOverInfo(GameOverInfo.OverJob.ASSASSIN, assassin.getUsername(), assassin.getNickname(), assassin.getMissionClearAt());
                gameOverInfo.putDetail(GameOverInfo.OverType.BAD, "안 들키면 암살 맞죠?", "원한을 사지 않게 조심하세요. 경찰은 그를 막을 수 없거든요..", "청부 살인 성공");
                return gameOverInfo;
            }
        }

        // 4. 스파이 승리 체크
        var spy = room.getSpy();
        if (spy != null) {
            if(spy.getClearMissionPhase() == spy.getMaxMissionPhase()) {
                gameOverInfo = new GameOverInfo(GameOverInfo.OverJob.SPY, spy.getUsername(), spy.getNickname(), spy.getMissionClearAt());
                gameOverInfo.putDetail(GameOverInfo.OverType.BAD, "♚♚히어로즈 오브 더 스☆톰♚♚가입시$$전원 카드팩☜☜", "도시는 그의 놀이터가 아닐까요?", "도시 기밀 유출");
                return gameOverInfo;
            }
        }

        return null;
    }

    @Override
    public void onExit(Room room) {

    }
}
