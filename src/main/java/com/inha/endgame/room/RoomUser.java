package com.inha.endgame.room;

import com.inha.endgame.user.User;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class RoomUser implements Serializable {
    private final String username;
    private final String nickname;
    private final rVector3D pos;
    private final RoomUserType roomUserType;

    public RoomUser(User user) {
        this.username = user.getUsername();
        this.nickname = user.getUsername();
        this.pos = new rVector3D(0,0,0);
        this.roomUserType = RoomUserType.USER;
    }

    public RoomUser(String username, String nickname, rVector3D pos, RoomUserType roomUserType) {
        this.username = username;
        this.nickname = nickname;
        this.pos = pos;
        this.roomUserType = roomUserType;
    }

    public static RoomUser createNpc() {
        var username = UUID.randomUUID().toString();
        var nickname = UUID.randomUUID().toString();
        var pos = new rVector3D(0,0,0);

        return new RoomUser(username, nickname, pos, RoomUserType.NPC);
    }

    @Override
    public String toString() {
        return "RoomUser{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", pos=" + pos +
                '}';
    }
}
