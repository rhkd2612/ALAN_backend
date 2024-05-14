package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReportUserRequest implements ClientRequest {
    @Schema(description = "REPORT_USER", defaultValue = "REPORT_USER")
    private RequestType type;
    private String reportUsername;
    private String targetUsername;
    private int roomId;
}
