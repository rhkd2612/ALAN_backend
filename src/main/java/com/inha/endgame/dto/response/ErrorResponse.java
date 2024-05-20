package com.inha.endgame.dto.response;

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ErrorResponse implements ClientResponse {
	@Schema(description = "ERROR", defaultValue = "ERROR")
	private final ResponseType type;
	private final String errMessage;

	public ErrorResponse(String errMessage) {
		this.type = ResponseType.ERROR;
		this.errMessage = errMessage;
	}

	public String getErrMessage() {
		return errMessage;
	}
}
