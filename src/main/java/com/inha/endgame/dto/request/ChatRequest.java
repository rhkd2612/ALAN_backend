package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatRequest implements ClientRequest {
    @Schema(description = "CHAT", defaultValue = "CHAT")
    private RequestType type;
    private String nickname;
    private String content;
    private Date chatAt;
    private long roomId;
}
