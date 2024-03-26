
package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class AddUserRequest implements ClientRequest {
	@Schema(description = "ADD_USER", defaultValue = "ADD_USER")
	private RequestType type;
	private String username;
	private String nickname;
	private long roomId;
}
