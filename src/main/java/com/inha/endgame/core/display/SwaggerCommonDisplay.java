package com.inha.endgame.core.display;

import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.dto.request.StartRoomRequest;
import com.inha.endgame.dto.request.TestRequest;
import com.inha.endgame.dto.response.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Req/Res를 클라이언트에 전달하기 위해 Swagger Display용
 */
@RestController
@Tag(name = "Common")
public class SwaggerCommonDisplay {
    @GetMapping("/TEST")
    void test(TestRequest testRequest,
              TestResponse testResponse) throws IllegalAccessException {
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

    @GetMapping("/PLAY_ROOM_INFO")
    void test(PlayRoomInfoResponse PlayRoomInfoResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/ERROR")
    void error(ErrorResponse ErrorResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }
}
