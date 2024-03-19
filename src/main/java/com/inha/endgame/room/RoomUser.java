package com.inha.endgame.room;

import com.inha.endgame.user.User;
import lombok.Getter;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.Serializable;

@Getter
public class RoomUser implements Serializable {
    private String userId;
    private String username;
    private rVector3D pos;

    public RoomUser(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.pos = new rVector3D(0,0,0);
    }

    @Override
    public String toString() {
        return "RoomUser{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", pos=" + pos +
                '}';
    }
}
