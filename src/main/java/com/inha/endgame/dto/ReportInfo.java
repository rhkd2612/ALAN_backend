package com.inha.endgame.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ReportInfo {
    private String reportUsername;
    private Date nextReportAvailAt;
    private String targetUsername;
    private Date highlightStartAt;
    private Date highlightEndAt;
}