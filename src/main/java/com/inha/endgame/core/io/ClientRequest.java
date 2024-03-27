
package com.inha.endgame.core.io;

import com.inha.endgame.dto.request.UpdateUserRequest;
import com.inha.endgame.dto.request.StartRoomRequest;
import com.inha.endgame.dto.request.TestRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inha.endgame.dto.request.AddUserRequest;

@JsonTypeInfo( //
		use = JsonTypeInfo.Id.NAME, //
		include = JsonTypeInfo.As.PROPERTY, //
		property = "type", //
		// defaultImpl = Event.class, //
		visible = true)
@JsonSubTypes({
		@Type(value = TestRequest.class, name = "TEST"),
		@Type(value = AddUserRequest.class, name = "ADD_USER"), //
		@Type(value = StartRoomRequest.class, name = "START_ROOM"),
		@Type(value = UpdateUserRequest.class, name = "UPDATE_USER"),
})
public interface ClientRequest {
	RequestType getType();
}