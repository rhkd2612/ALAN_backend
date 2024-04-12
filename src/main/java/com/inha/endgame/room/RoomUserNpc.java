package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.user.UserState;
import com.inha.endgame.user.NpcState;
import com.inha.endgame.user.User;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

@Getter
public class RoomUserNpc extends RoomUser {
    @JsonIgnore
    private Date stateUpAt = null;
    @JsonIgnore
    private NpcState npcState = NpcState.STOP;
    @JsonIgnore
    private boolean animPlay = false;

    public RoomUserNpc(User user) {
        super(user);
    }

    public RoomUserNpc(String username, String nickname, rVector3D pos, rVector3D rot, RoomUserType roomUserType) {
        super(username, nickname, pos, rot, roomUserType);
    }

    public synchronized void rollState() {
        if(this.stateUpAt == null || !this.getUserState().equals(UserState.NORMAL))
            return;

        Date now = new Date();
        if(now.after(stateUpAt)) {
            this.animPlay = false;
            stateUpAt = new Date(now.getTime() + RandomUtils.nextInt(1000, 10000));

            // 추후 ANIM 추가
            var nextBehavior = RandomUtils.nextInt(0, 2);
            if(nextBehavior == 1) {
                this.npcState = NpcState.MOVE;
                this.setRot(new rVector3D(0, RandomUtils.nextInt(0, 360), 0));
                this.setVelocity(1);
                this.setAnim(1);
            } else {
                this.npcState = NpcState.STOP;
                this.setVelocity(0);
                this.setAnim(0);
            }
        }
    }

    public void startNpc() {
        this.stateUpAt = new Date();
    }

    public rVector3D getNextPos(int frameCount) {
        if(!this.getUserState().equals(UserState.NORMAL))
            return this.getPos();

        rVector3D result = this.getPos().add(this.getPos().normalize(this.getRot(), this.getVelocity(), frameCount));

        if(result.getX() < Room.minX || result.getX() > Room.maxX)
            result.setX(this.getPos().getX());
        if(result.getZ() < Room.minZ || result.getZ() > Room.maxZ)
            result.setZ(this.getPos().getZ());

        return result;
    }


    public void startAnim(int animNum, Date endAt) {
        this.setAnim(animNum);

        this.setVelocity(0);
        this.animPlay = true;
        this.npcState = NpcState.ANIM;
        this.stateUpAt = endAt;
    }
}
