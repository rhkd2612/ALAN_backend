package com.inha.endgame.room;

import com.inha.endgame.core.excel.JsonReader;
import com.inha.endgame.user.CrimeType;
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
    private CrimeType crimeType = CrimeType.NONE;

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
        this.roomUserType = roomUserType;
    }

    public RoomUser(String username, String nickname, rVector3D pos, rVector3D rot, RoomUserType roomUserType, CrimeType crimeType) {
        this.username = username;
        this.nickname = nickname;
        this.pos = pos;
        this.rot = rot;
        this.velocity = 1;
        this.anim = 0;
        this.color = RandomUtils.nextInt() % 3;
        this.roomUserType = roomUserType;
        this.crimeType = crimeType;
    }

    public static List<RoomUser> createNpc(int npcCount) {
        var fileName = "map";
        var key = "map_size";

        RoomService.minX = JsonReader._int(JsonReader.model(fileName, key,"mapXmin"));
        RoomService.maxX = JsonReader._int(JsonReader.model(fileName, key,"mapXmax"));
        RoomService.minZ = JsonReader._int(JsonReader.model(fileName, key,"mapZmin"));
        RoomService.maxZ = JsonReader._int(JsonReader.model(fileName, key,"mapZmax"));

        // TODO inner 관련된거 추가?
        var npcSpawnMinX = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer","posXmin"));
        var npcSpawnMaxX = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer","posXmax"));
        var npcSpawnMinZ = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer","posZmin"));
        var npcSpawnMaxZ = JsonReader._int(JsonReader.model("spawn", "spawn_npc_outer","posZmax"));

        ArrayList<RoomUser> npcs = new ArrayList<>();

        for(int i = 0; i < npcCount; i++) {
            var username = UUID.randomUUID().toString();
            var nickname = "NPC_" + i;

            var pos = new rVector3D(npcSpawnMinX + (float)(Math.random() * (npcSpawnMaxX - npcSpawnMinX)), 0, npcSpawnMinZ + (float)(Math.random() * (npcSpawnMaxZ - npcSpawnMinZ)));
            var rot = new rVector3D(0, 0, 0);

            npcs.add(new RoomUserNpc(username, nickname, pos, rot, RoomUserType.NPC));
        }

        return npcs;
    }

    public void updateUser(RoomUser roomUser, boolean canMove) {
        if(!this.userState.equals(UserState.STUN)) {
            if(canMove)
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
        this.anim = RandomUtils.nextInt(3, 4);
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

        var copSpawnMinX = JsonReader._int(JsonReader.model("spawn", "spawn_cop","posXmin"));
        var copSpawnMaxX = JsonReader._int(JsonReader.model("spawn", "spawn_cop","posXmax"));
        var copSpawnMinZ = JsonReader._int(JsonReader.model("spawn", "spawn_cop","posZmin"));
        var copSpawnMaxZ = JsonReader._int(JsonReader.model("spawn", "spawn_cop","posZmax"));

        var pos = new rVector3D(copSpawnMinX + (float)(Math.random() * (copSpawnMaxX - copSpawnMinX)), 0, copSpawnMinZ + (float)(Math.random() * (copSpawnMaxZ - copSpawnMinZ)));
        this.pos = pos;
    }

    public void beCrime() {
        this.roomUserType = RoomUserType.USER;
        this.crimeType = CrimeType.SPY; // 일단 한개이므로..

        // TODO inner 관련된거 추가?
        var crimeSpawnMinX = JsonReader._int(JsonReader.model("spawn", "spawn_criminal_outer","posXmin"));
        var crimeSpawnMaxX = JsonReader._int(JsonReader.model("spawn", "spawn_criminal_outer","posXmax"));
        var crimeSpawnMinZ = JsonReader._int(JsonReader.model("spawn", "spawn_criminal_outer","posZmin"));
        var crimeSpawnMaxZ = JsonReader._int(JsonReader.model("spawn", "spawn_criminal_outer","posZmax"));

        var pos = new rVector3D(crimeSpawnMinX + (float)(Math.random() * (crimeSpawnMaxX - crimeSpawnMinX)), 0, crimeSpawnMinZ + (float)(Math.random() * (crimeSpawnMaxZ - crimeSpawnMinZ)));
        this.pos = pos;

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
