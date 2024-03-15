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

package com.example.test.dto.response;

import com.example.test.core.ClientResponse;
import com.example.test.core.ResponseType;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

/**
 * Just a dummy response used as a test.
 * 
 * @author L0G1C (David B) <a
 *         href=https://github.com/Binary-L0G1C/java-unity-websocket-connector>
 *         https://github.com/Binary-L0G1C/java-unity-websocket-connector </a>
 */
@Service
public class KemonomimiResponse extends ClientResponse {
	private Collection<String> kemonomimi = new HashSet<>();

	public KemonomimiResponse() {
		setType(ResponseType.KEMONOMIMI);

		HashSet<String> defaultSet = new HashSet<>();
		defaultSet.add("Ahri");
		defaultSet.add("Horo");
		defaultSet.add("Blake");
		defaultSet.add("Feyris");

		setKemonomimi(defaultSet);
	}

	public Collection<String> getKemonomimi() {
		return kemonomimi;
	}

	public void setKemonomimi(Collection<String> kemonomimi) {
		this.kemonomimi = kemonomimi;
	}
}
