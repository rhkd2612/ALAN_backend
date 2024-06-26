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

package com.inha.endgame.core.io;

import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;
import org.springframework.web.socket.WebSocketSession;

public class ClientEvent<T extends ClientRequest> implements ResolvableTypeProvider {

	private final T clientRequest;
	private final WebSocketSession session;
	private String username;

	public ClientEvent(T clientRequest, WebSocketSession session) {
		this.clientRequest = clientRequest;
		this.session = session;
	}

	public T getClientRequest() {
		return clientRequest;
	}

	public WebSocketSession getSession() {
		return session;
	}

	@Override
	public ResolvableType getResolvableType() {
		return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(clientRequest));
	}
}
