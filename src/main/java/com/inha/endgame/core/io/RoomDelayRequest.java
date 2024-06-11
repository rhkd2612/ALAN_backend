package com.inha.endgame.core.io;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public abstract class RoomDelayRequest implements ClientRequest {
    protected Date requestAt;
    protected Date responseAt;
    protected boolean needSuccess;
}
