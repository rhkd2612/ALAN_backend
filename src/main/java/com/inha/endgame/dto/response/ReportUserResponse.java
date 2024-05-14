package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import com.inha.endgame.dto.ReportInfo;
import com.inha.endgame.dto.eventlistener.RoomEventListener;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
public class ReportUserResponse implements ClientResponse {
    @Schema(description = "REPORT_USER", defaultValue = "REPORT_USER")
    private final ResponseType type;
    private final String reportUsername;
    private final Date nextReportAvailAt;
    private final String targetUsername;
    private final Date highlightStartAt;
    private final Date highlightEndAt;

    public ReportUserResponse(ReportInfo reportInfo) {
        this.type = ResponseType.REPORT_USER;
        this.reportUsername = reportInfo.getReportUsername();
        this.nextReportAvailAt = reportInfo.getNextReportAvailAt();
        this.targetUsername = reportInfo.getTargetUsername();
        this.highlightStartAt = reportInfo.getHighlightStartAt();
        this.highlightEndAt = reportInfo.getHighlightEndAt();
    }
}