package com.inha.endgame.room;


import com.inha.endgame.dto.ReportInfo;
import com.inha.endgame.dto.UseItemInfo;
import com.inha.endgame.room.event.AnimEvent;
import com.inha.endgame.user.CrimeType;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;

@Getter
public class Room {
    private final long roomId;
    private final Map<String, RoomUser> roomUsers;
    private final Map<String, RoomUser> roomNpcs;
    private String copUsername;
    private String spyUsername;
    private String boomerUsername;
    private String assassinUsername;

    private AnimEvent event;

    private RoomState curState;
    private RoomState nextState;

    private final Date createAt;
    private Date readyAt;

    private final List<CrimeType> remainCrimeTypes = new ArrayList<>();
    private int crimeCount = 0;

    private String hostNickname; // 방장

    private Set<UseItemInfo> recentItemUseAt = new ConcurrentSkipListSet<>();
    private Set<ReportInfo> recentReportAt = new ConcurrentSkipListSet<>();

    public Room(long roomId) {
        this.roomId = roomId;
        this.createAt = new Date();
        this.roomUsers = new ConcurrentHashMap<>();
        this.roomNpcs = new ConcurrentHashMap<>();
        this.curState = null;
        this.nextState = RoomState.NONE;

        for(var type : CrimeType.values()) {
            if(!type.equals(CrimeType.NONE))
                this.remainCrimeTypes.add(type);
        }
    }

    public CrimeType getRandomCrimeType() {
        if(this.crimeCount == this.remainCrimeTypes.size())
            return CrimeType.NONE;

        var r = RandomUtils.nextInt(0, remainCrimeTypes.size() - this.crimeCount);
        var crimeType = remainCrimeTypes.get(r);

        var temp = remainCrimeTypes.get(remainCrimeTypes.size() - this.crimeCount - 1);
        remainCrimeTypes.set(remainCrimeTypes.size() - this.crimeCount - 1, crimeType);
        remainCrimeTypes.set(r, temp);

        this.crimeCount++;

        return crimeType;
    }

    public void setCopUsername(String copUsername) {
        this.copUsername = copUsername;
    }

    public void setSpyUsername(String spyUsername) {
        this.spyUsername = spyUsername;
    }

    public void setBoomerUsername(String boomerUsername) {
        this.boomerUsername = boomerUsername;
    }

    public void setAssassinUsername(String assassinUsername) {
        this.assassinUsername = assassinUsername;
    }

    public List<RoomUser> getAllMembers() {
        List<RoomUser> result = new ArrayList<>();

        result.addAll(this.roomUsers.values());
        result.addAll(this.roomNpcs.values());

        return result;
    }

    public Map<String, RoomUser> getAllMembersMap() {
        Map<String,RoomUser> result = new HashMap<>();

        result.putAll(this.roomUsers);
        result.putAll(this.roomNpcs);

        return result;
    }

    public RoomUserCop getCop() {
        return (RoomUserCop)(this.roomUsers.get(this.copUsername));
    }

    public RoomUserCrimeSpy getSpy() {
        if(this.spyUsername == null)
            return null;
        return (RoomUserCrimeSpy) (this.roomUsers.get(this.spyUsername));
    }

    public RoomUserCrimeBoomer getBoomer() {
        if(this.boomerUsername == null)
            return null;
        return (RoomUserCrimeBoomer) (this.roomUsers.get(this.boomerUsername));
    }

    public RoomUserCrimeAssassin getAssassin() {
        if(this.assassinUsername == null)
            return null;
        return (RoomUserCrimeAssassin) (this.roomUsers.get(this.assassinUsername));
    }

    public void setEvent(AnimEvent event) {
        this.event = event;
    }

    public void setRoomNpc(int npcCount, List<rVector3D> randomNpcPos) {
        if(this.curState != RoomState.NONE)
            throw new IllegalStateException("NONE 상태일 때만 세팅 가능합니다.");

        this.roomNpcs.clear();

        List<RoomUser> npcs = RoomUser.createNpc(npcCount, randomNpcPos);
        npcs.forEach(npc -> this.roomNpcs.put(npc.getUsername(), npc));
    }

    public synchronized void join(RoomUser user) {
        if(this.curState == RoomState.END)
            throw new IllegalStateException("종료된 방입니다.");
        if(this.roomUsers.containsKey(user.getUsername())) {
            return;
        }

        if(this.roomUsers.isEmpty())
            this.hostNickname = user.getNickname();

        this.roomUsers.put(user.getUsername(), user);
    }

    public synchronized void start() {
        if(this.curState != RoomState.NONE)
            throw new IllegalStateException("시작할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.READY;
        this.readyAt = new Date();
    }

    public Date getPlayAt() {
        return new Date(this.readyAt.getTime() + 5000);
    }

    public Date getEndAt() {
        return new Date(this.readyAt.getTime() + 1000000);
    }

    public synchronized void play() {
        if(this.curState != RoomState.READY)
            throw new IllegalStateException("시작할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.PLAY;
    }

    public synchronized void end() {
        if(this.curState != RoomState.PLAY)
            throw new IllegalStateException("종료할 수 없는 상태의 방입니다.");
        this.nextState = RoomState.END;
    }

    public synchronized void recordUseItem(UseItemInfo useItemInfo) {
        this.recentItemUseAt.add(useItemInfo);
    }

    public synchronized void recordReportUser(ReportInfo reportInfo) {
        this.recentReportAt.add(reportInfo);
    }

    public List<rVector3D> getRecentUseItemInfo() {
        List<rVector3D> result = new ArrayList<>();
        Date now = new Date();
        var copySet = new CopyOnWriteArraySet<>(this.recentItemUseAt);
        for(var iter : copySet) {
            if(now.after(new Date(iter.getItemUseAt().getTime() + 5000)))
                this.recentItemUseAt.remove(iter);
            else
                result.add(iter.getItemPos());
        }

        return result;
    }

    public List<ReportInfo> getRecentReportUserInfo() {
        List<ReportInfo> result = new ArrayList<>();
        Date now = new Date();
        var copySet = new CopyOnWriteArraySet<>(this.recentReportAt);
        for(var iter : copySet) {
            if(now.after(new Date(iter.getHighlightEndAt().getTime())))
                this.recentReportAt.remove(iter);
            else
                result.add(iter);
        }

        return result;
    }

    public synchronized void kick(RoomUser user) {
        this.roomUsers.remove(user.getUsername());
    }

    // 일반적으로는 변경하면 안됨
    public void setCurState(RoomState curState) {
        this.curState = curState;
    }

    public void setNextState(RoomState nextState) {
        this.nextState = nextState;
    }
}
