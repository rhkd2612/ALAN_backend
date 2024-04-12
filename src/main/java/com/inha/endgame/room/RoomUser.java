package com.inha.endgame.room;

import com.inha.endgame.user.UserState;
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
    private UserState userState = UserState.NORMAL;

    public RoomUser(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
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

    public static List<RoomUser> createNpc(int npcCount) {
        ArrayList<RoomUser> npcs = new ArrayList<>();

        for(int i = 0; i < npcCount; i++) {
            var username = UUID.randomUUID().toString();
            var nickname = "NPC_" + i;

            var pos = new rVector3D(Room.minX + (float)(Math.random() * (Room.maxX - Room.minX)), 0, Room.minZ + (float)(Math.random() * (Room.maxZ - Room.minZ)));
            var rot = new rVector3D(0, 0, 0);

            npcs.add(new RoomUserNpc(username, nickname, pos, rot, RoomUserType.NPC));
        }

        return npcs;
    }

    public void updateUser(RoomUser roomUser) {
        if(!this.userState.equals(UserState.STUN)) {
            this.pos = roomUser.getPos();
            this.velocity = roomUser.getVelocity();
        } else {
            this.velocity = 0;
        }

        this.rot = roomUser.getRot();
        this.anim = roomUser.getAnim();
        this.color = roomUser.getColor();
    }

    public void stunned() {
        if(this.userState.equals(UserState.DIE))
            return;

        this.userState = UserState.STUN;
        this.velocity = 0;
    }

    public void releaseStun() {
        if(this.userState.equals(UserState.DIE))
            return;
        this.userState = UserState.NORMAL;
    }

    public void die() {
        this.userState = UserState.DIE;
        this.velocity = 0;
    }

    public void beCop() {
        this.roomUserType = RoomUserType.COP;
    }

    public void beCrime() {
        this.roomUserType = RoomUserType.USER;

        // 아이템 지급 등
    }

    public void setAnim(int anim) {
        this.anim = anim;
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
