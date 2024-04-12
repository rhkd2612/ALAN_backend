package com.inha.endgame.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inha.endgame.user.UserState;
import com.inha.endgame.user.AimState;
import com.inha.endgame.user.CopAttackState;
import com.inha.endgame.user.User;
import lombok.Getter;

import java.util.Date;

@Getter
public class RoomUserCop extends RoomUser {
    @JsonIgnore
    private Date availAimAt = new Date();
    @JsonIgnore
    private Date availShotAt = new Date();
    @JsonIgnore
    private Date releaseStunAt = new Date();
    @JsonIgnore
    private rVector3D targetAimPos = null;
    @JsonIgnore
    private String targetUsername = null;
    @JsonIgnore
    private CopAttackState copAttackState = CopAttackState.NONE;
    public static final int MAX_AIM_TIME = 5000;
    public static final int AVAIL_SHOT_TIME = 3000;

    public RoomUserCop(User user) {
        super(user);
    }

    public RoomUserCop(String username, String nickname, rVector3D pos, rVector3D rot, RoomUserType roomUserType) {
        super(username, nickname, pos, rot, roomUserType);
    }

    public RoomUserCop(RoomUser roomUser) {
        super(roomUser.getUsername(), roomUser.getNickname(), roomUser.getPos(), roomUser.getRot(), roomUser.getRoomUserType());
    }

    public synchronized void aiming(AimState aimState, rVector3D targetPos) {
        Date now = new Date();
        if(aimState.equals(AimState.START) && now.before(this.availAimAt))
            throw new IllegalStateException("아직 사용할 수 없습니다.");

        this.availAimAt = new Date(now.getTime() + MAX_AIM_TIME);
        this.copAttackState = CopAttackState.AIM;
        this.targetAimPos = targetPos;
    }

    public synchronized void endAimingAndStun() {
        Date now = new Date();
        this.availAimAt = new Date(now.getTime() + MAX_AIM_TIME);
        this.copAttackState = CopAttackState.NONE;
        this.releaseStunAt = now;
        this.availShotAt = availAimAt;
        this.targetAimPos = null;
        this.targetUsername = null;
    }

    public synchronized void stun(RoomUser targetUser) {
        Date now = new Date();
        if(now.before(this.availShotAt))
            throw new IllegalStateException("아직 사용할 수 없습니다.");

        if(!this.copAttackState.equals(CopAttackState.AIM))
            throw new IllegalStateException("조준 상태에서만 사용할 수 있습니다.");

        if(!targetUser.getUserState().equals(UserState.NORMAL))
            throw new IllegalStateException("제압될 수 있는 상태가 아닙니다.");

        // FIXME 거리 수정
        if(targetUser.getPos().distance(this.getPos()) > 999.0)
            throw new IllegalStateException("거리가 너무 멀어 제압할 수 없습니다.");

        this.copAttackState = CopAttackState.STUN;
        this.releaseStunAt = new Date(now.getTime() + MAX_AIM_TIME);
        this.availShotAt = new Date(now.getTime() + AVAIL_SHOT_TIME);
        this.targetUsername = targetUser.getUsername();
    }

    public void checkShot(RoomUser targetUser) {
        Date now = new Date();
        if(now.after(this.releaseStunAt))
            throw new IllegalStateException("제압이 풀렸습니다.");

        if(now.before(this.availShotAt))
            throw new IllegalStateException("아직 사격할 수 없습니다.");

        if(!this.targetUsername.equals(targetUser.getUsername()))
            throw new IllegalStateException("타겟이 없습니다.");

        if(!this.copAttackState.equals(CopAttackState.STUN) && targetUser.getUserState().equals(UserState.STUN))
            throw new IllegalStateException("제압 상태에서만 사용할 수 있습니다.");
    }
}
