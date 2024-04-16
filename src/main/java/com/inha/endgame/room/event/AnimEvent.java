package com.inha.endgame.room.event;

import com.inha.endgame.core.excel.JsonReader;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class AnimEvent {
    private int animNum;
    private Date nextAnimAt;
    private boolean isAnnounce;

    public AnimEvent() {
        resetAnimEvent();
    }

    public synchronized void resetAnimEvent() {
        List<Object> motions = JsonReader.models("motion");
        var minMotionNum = 987654321;
        var motionCount = 0;
        var maxAnimTime = 0;

        for(var i = 0; i < motions.size(); i++) {
            var json = (LinkedHashMap) motions.get(i);
            var screenMotion = JsonReader._bool(json.get("screenMotion"));

            if(screenMotion) {
                motionCount++;
                minMotionNum = Math.min(minMotionNum, JsonReader._int(json.get("motionNo")));
                maxAnimTime = Math.max(maxAnimTime, JsonReader._int(json.get("motionTime")));
            }
        }

        var minMotionCycleTime = JsonReader._time(JsonReader.model("screen","screen","screenMotionCycleMin"));
        var maxMotionCycleTime = JsonReader._time(JsonReader.model("screen","screen","screenMotionCycleMax"));
        if(motionCount > 0) {
            this.animNum = RandomUtils.nextInt(minMotionNum, minMotionNum + motionCount - 1);
            this.nextAnimAt = new Date(new Date().getTime() + RandomUtils.nextInt(minMotionCycleTime, maxMotionCycleTime));
        }

        this.isAnnounce = false;
    }

    public void setAnnounce(boolean isAnnounce) {
        this.isAnnounce = isAnnounce;
    }
}