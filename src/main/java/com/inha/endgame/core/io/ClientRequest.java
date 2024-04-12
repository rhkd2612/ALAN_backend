
package com.inha.endgame.core.io;

import com.inha.endgame.dto.request.*;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( //
		use = JsonTypeInfo.Id.NAME, //
		include = JsonTypeInfo.As.PROPERTY, //
		property = "type", //
		// defaultImpl = Event.class, //
		visible = true)
@JsonSubTypes({
		@Type(value = TestRequest.class, name = "TEST"),
		@Type(value = AddUserRequest.class, name = "ADD_USER"),
		@Type(value = CheckUserRequest.class, name = "CHECK_USER"),
		@Type(value = StartRoomRequest.class, name = "START_ROOM"),
		@Type(value = SettingRoomRequest.class, name = "SETTING_ROOM"),
		@Type(value = UpdateUserRequest.class, name = "UPDATE_USER"),
		@Type(value = PingRequest.class, name = "PING"),
		@Type(value = NetworkDelayRequest.class, name = "NETWORK_DELAY"),
		@Type(value = AimRequest.class, name = "AIM"),
		@Type(value = StunRequest.class, name = "STUN"),
		@Type(value = ShotRequest.class, name = "SHOT"),
})
public interface ClientRequest {
	RequestType getType();
}