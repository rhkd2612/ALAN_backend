package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.dto.request.ChatRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class ChatResponse implements ClientResponse {
    @Schema(description = "CHAT", defaultValue = "CHAT")
    private final ResponseType type;
    private final String nickname;
    private final String content;
    private final Date chatAt;

    public ChatResponse(ChatRequest chatRequest) {
        this.type = ResponseType.CHAT;
        this.nickname = chatRequest.getNickname();
        this.content = chatRequest.getContent();
        this.chatAt = chatRequest.getChatAt();
    }
}
