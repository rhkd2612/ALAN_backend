package com.inha.endgame.room;

import com.inha.endgame.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class RoomUser implements Serializable {
    private final String userId;
    private final String username;
    private final rVector3D pos;
    private final RoomUserType roomUserType;

    public RoomUser(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.pos = new rVector3D(0,0,0);
        this.roomUserType = RoomUserType.USER;
    }

    public RoomUser(String userId, String username, rVector3D pos, RoomUserType roomUserType) {
        this.userId = userId;
        this.username = username;
        this.pos = pos;
        this.roomUserType = roomUserType;
    }

    public static RoomUser createNpc() {
        var userId = UUID.randomUUID().toString();
        var username = UUID.randomUUID().toString();
        var pos = new rVector3D(0,0,0);

        return new RoomUser(userId, username, pos, RoomUserType.NPC);
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
