package com.inha.endgame.config;

import com.inha.endgame.core.unitysocket.UnitySocketWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebConfig implements WebSocketConfigurer {

	@Autowired
    UnitySocketWebSocketHandler unitySocketWebSocketHandler;

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(unitySocketWebSocketHandler, "/unitysocket").setAllowedOrigins("*");
	}
}
