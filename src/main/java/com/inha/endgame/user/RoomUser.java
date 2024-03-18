package com.inha.endgame.user;

import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

@Getter
public class RoomUser {
    private String userId;
    private String username;
    private Vector3D pos;

    public RoomUser(String userId, String username, Vector3D pos) {
        this.userId = userId;
        this.username = username;
        this.pos = pos;
    }
}
