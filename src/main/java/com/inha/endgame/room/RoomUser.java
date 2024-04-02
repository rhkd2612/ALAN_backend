package com.inha.endgame.room;

import com.inha.endgame.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class RoomUser implements Serializable {
    private String username;
    private String nickname;
    private rVector3D pos;
    private rVector3D rot;
    private float velocity;
    private int anim;
    private int color;
    private RoomUserType roomUserType;

    public RoomUser(User user) {
        this.username = user.getUsername();
        this.nickname = user.getUsername();
        this.pos = new rVector3D(0,0,0);
        this.rot = new rVector3D(0,0,0);
        this.velocity = 1;
        this.roomUserType = RoomUserType.USER;
    }

    public RoomUser(String username, String nickname, rVector3D pos, rVector3D rot, RoomUserType roomUserType) {
        this.username = username;
        this.nickname = nickname;
        this.pos = pos;
        this.rot = rot;
        this.velocity = 1;
        this.anim = 0;
        this.color = RandomUtils.nextInt() % 3;
        this.roomUserType = roomUserType;
    }

    public static List<RoomUser> createNpc() {
        ArrayList<RoomUser> npcs = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            var username = UUID.randomUUID().toString();
            var nickname = "NPC_" + i;

            var pos = new rVector3D(RandomUtils.nextInt(0, 5), 0, RandomUtils.nextInt(0, 5));
            var rot = new rVector3D(0, 0, 0);

            npcs.add(new RoomUserNpc(username, nickname, pos, rot, RoomUserType.NPC));
        }

        return npcs;
    }

    public void setPos(rVector3D pos) {
        this.pos = pos;
    }

    public void setRot(rVector3D rot) {
        this.rot = rot;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
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
