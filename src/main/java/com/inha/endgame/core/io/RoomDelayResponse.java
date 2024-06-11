package com.inha.endgame.core.io;

import lombok.Setter;

import java.util.Date;

@Setter
public abstract class RoomDelayResponse implements ClientResponse {
    protected Date responseAt;
}
