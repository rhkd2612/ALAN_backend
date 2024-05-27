
package com.inha.endgame.core.io;

import com.inha.endgame.dto.request.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		visible = true)
@JsonSubTypes({
		@Type(value = TestRequest.class, name = "TEST"),
		@Type(value = AddUserRequest.class, name = "ADD_USER"),
		@Type(value = CheckUserRequest.class, name = "CHECK_USER"),
		@Type(value = StartRoomRequest.class, name = "START_ROOM"),
		@Type(value = LeaveRoomRequest.class, name = "LEAVE_ROOM"),
		@Type(value = SettingRoomRequest.class, name = "SETTING_ROOM"),
		@Type(value = UpdateUserRequest.class, name = "UPDATE_USER"),
		@Type(value = PingRequest.class, name = "PING"),
		@Type(value = NetworkDelayRequest.class, name = "NETWORK_DELAY"),
		@Type(value = AimRequest.class, name = "AIM"),
		@Type(value = StunRequest.class, name = "STUN"),
		@Type(value = ShotRequest.class, name = "SHOT"),
		@Type(value = PlayMissionRequest.class, name = "PLAY_MISSION"),
		@Type(value = UseItemRequest.class, name = "USE_ITEM"),
		@Type(value = AssassinKillRequest.class, name = "ASSASSIN_KILL"),
		@Type(value = CreateRoomRequest.class, name = "CREATE_ROOM"),
		@Type(value = RoomListRequest.class, name = "ROOM_LIST"),
		@Type(value = ReportUserRequest.class, name = "REPORT_USER"),
		@Type(value = ChatRequest.class, name = "CHAT"),
		@Type(value = ReconnectRequest.class, name = "RECONNECT"),
		@Type(value = GameOverConfirmRequest.class, name = "GAME_OVER_CONFIRM"),
})
public interface ClientRequest {
	RequestType getType();
}