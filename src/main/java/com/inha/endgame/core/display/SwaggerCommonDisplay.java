package com.inha.endgame.core.display;

import com.inha.endgame.dto.request.AddUserRequest;
import com.inha.endgame.dto.request.StartRoomRequest;
import com.inha.endgame.dto.request.TestRequest;
import com.inha.endgame.dto.response.AddUserResponse;
import com.inha.endgame.dto.response.ErrorResponse;
import com.inha.endgame.dto.response.StartRoomResponse;
import com.inha.endgame.dto.response.TestResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Req/Res를 클라이언트에 전달하기 위해 Swagger Display용
 */
@RestController
@Tag(name = "Common")
public class SwaggerCommonDisplay {
    @GetMapping("/test")
    void test(TestRequest testRequest,
              TestResponse testResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/addUser")
    void test(AddUserRequest AddUserRequest,
              AddUserResponse AddUserResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/startRoom")
    void test(StartRoomRequest StartRoomRequest,
              StartRoomResponse StartRoomResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }

    @GetMapping("/error")
    void error(ErrorResponse ErrorResponse) throws IllegalAccessException {
        throw new IllegalAccessException("절대 직접 호출하지 않는다. (Swagger용)");
    }
}
