/*
 * Copyright 2017 L0G1C (David B) - logiclodge.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.inha.endgame.dto.response;

import com.inha.endgame.core.ClientResponse;
import com.inha.endgame.core.ResponseType;
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
