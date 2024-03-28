package com.inha.endgame.room;

import com.inha.endgame.user.NpcState;
import com.inha.endgame.user.User;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

@Getter
public class RoomUserNpc extends RoomUser {
    private Date stateUpAt = null;
    private NpcState npcState = NpcState.STOP;
    private boolean animPlay = false;

    public RoomUserNpc(User user) {
        super(user);
    }

    public RoomUserNpc(String username, String nickname, rVector3D pos, rVector3D rot, RoomUserType roomUserType) {
        super(username, nickname, pos, rot, roomUserType);
    }

    public synchronized void rollState() {
        if(this.stateUpAt == null || this.npcState.equals(NpcState.DIE) || this.animPlay)
            return;

        this.animPlay = false;

        Date now = new Date();
        if(now.after(stateUpAt)) {
            stateUpAt = new Date(now.getTime() + RandomUtils.nextInt(1000, 10000));

            // 추후 ANIM 추가
            var nextBehavior = RandomUtils.nextInt(0, 2);
            if(nextBehavior == 1) {
                this.npcState = NpcState.MOVE;
                this.setRot(new rVector3D(0, RandomUtils.nextInt(0, 360), 0));
                this.setVelocity(1);
            } else {
                this.npcState = NpcState.STOP;
                this.setVelocity(0);
            }
        }
    }

    public void startNpc() {
        this.stateUpAt = new Date();
    }

    public rVector3D getNextPos(int frameCount) {
        return this.getPos().add(this.getPos().normalize(this.getRot(), this.getVelocity(), frameCount));
    }

    public void startAnim(Date endAt) {
        this.animPlay = true;
        this.stateUpAt = endAt;
    }
}