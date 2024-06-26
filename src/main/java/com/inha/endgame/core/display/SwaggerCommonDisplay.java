package com.inha.endgame.core.display;

import com.inha.endgame.dto.request.*;
import com.inha.endgame.dto.response.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Req/Res를 클라이언트에 전달하기 위해 Swagger Display용으로 변형
 */
@RestController
@Tag(name = "Common")
public class SwaggerCommonDisplay {
    @GetMapping("/TEST")
    void test(TestRequest testRequest) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/CREATE_ROOM")
    void test(CreateRoomRequest CreateRoomRequest,
              CreateRoomResponse CreateRoomResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/ROOM_LIST")
    void test(RoomListRequest RoomListRequest,
              RoomListResponse RoomListResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/CHECK_USER")
    void test(CheckUserRequest CheckUserRequest,
              CheckUserResponse CheckUserResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/ADD_USER")
    void test(AddUserRequest AddUserRequest,
              AddUserResponse AddUserResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/START_ROOM")
    void test(StartRoomRequest StartRoomRequest,
              StartRoomResponse StartRoomResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/LEAVE_ROOM")
    void error(LeaveRoomRequest LeaveRoomRequest, LeaveRoomResponse LeaveRoomResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/SETTING_ROOM")
    void test(SettingRoomRequest SettingRoomRequest,
              SettingRoomResponse SettingRoomResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/PLAY_ROOM_INFO")
    void test(PlayRoomInfoResponse PlayRoomInfoResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/UPDATE_USER")
    void test(UpdateUserRequest UpdateUserRequest) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/EVENT_INFO")
    void test(EventInfoResponse EventInfoResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/SELECT_JOB")
    void test(SelectJobResponse SelectJobResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/AIM")
    void test(AimRequest AimRequest, AimResponse AimResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/STUN")
    void test(StunRequest StunRequest, StunResponse StunResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/SHOT")
    void test(ShotRequest ShotRequest, ShotResponse ShotResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/PLAY_MISSION")
    void test(PlayMissionRequest PlayMissionRequest, PlayMissionResponse PlayMissionResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/USE_ITEM")
    void error(UseItemRequest UseItemRequest, UseItemResponse UseItemResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/ASSASSIN_KILL")
    void error(AssassinKillRequest AssassinKillRequest, AssassinKillResponse AssassinKillResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/REPORT_USER")
    void error(ReportUserRequest ReportUserRequest, ReportUserResponse ReportUserResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/CHAT")
    void error(ChatRequest ChatRequest, ChatResponse ChatResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/ERROR")
    void error(ErrorResponse ErrorResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/RECONNECT")
    void error(ReconnectRequest ReconnectRequest, ReconnectResponse ReconnectResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/PING")
    void error(PingRequest PingRequest, PingResponse PingResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/GAME_OVER")
    void error(GameOverConfirmRequest GameOverConfirmRequest, GameOverResponse GameOverResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/NETWORK_DELAY")
    void error(NetworkDelayRequest NetworkDelayRequest) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }
}
