package com.inha.endgame.room.event;

import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;

@Getter
public class AnimEvent {
    private int animNum;
    private Date nextAnimAt;
    private boolean isAnnounce;

    public AnimEvent() {
        resetAnimEvent();
    }

    public synchronized void resetAnimEvent() {
        System.out.println("reset event");

        this.animNum = RandomUtils.nextInt(1, 4);
        this.nextAnimAt = new Date(new Date().getTime() + RandomUtils.nextInt(10000, 30000));
        isAnnounce = false;
    }

    public void setAnnounce(boolean announce) {
        isAnnounce = announce;
    }
}
