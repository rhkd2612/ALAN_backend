package com.inha.endgame.dto.request;

import com.inha.endgame.core.io.ClientRequest;
import com.inha.endgame.core.io.RequestType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;


@Getter
public class TestRequest implements ClientRequest {
	@Schema(description = "TEST", defaultValue = "TEST")
	protected RequestType type = RequestType.TEST;
}
