
package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class CheckUserRequest implements ClientRequest {
	@Schema(description = "CHECK_USER", defaultValue = "CHECK_USER")
	private RequestType type;
	private String username;
	private long roomId;
}
