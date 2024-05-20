package com.inha.endgame.dto;

import com.inha.endgame.room.rVector3D;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UseItemInfo {
    private Date itemUseAt;
    private rVector3D itemPos;
    private String username;
}
