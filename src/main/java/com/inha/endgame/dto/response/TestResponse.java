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

import com.inha.endgame.core.io.ClientResponse;
import com.inha.endgame.core.io.ResponseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class TestResponse implements ClientResponse {
	@Schema(description = "TEST", defaultValue = "TEST")
	private final ResponseType type;
	private final int answer;

	public TestResponse(int answer) {
		this.type = ResponseType.TEST;
		this.answer = answer;
	}
}