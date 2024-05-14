package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.core.unitysocket.SessionService;
import com.inha.endgame.dto.request.NetworkDelayRequest;
import com.inha.endgame.dto.request.TestRequest;
import com.inha.endgame.core.unitysocket.UnitySocketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TestEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger("TestEventListener");

    private final UnitySocketService unitySocketService;
    private final SessionService sessionService;

    @EventListener
    public void onTestRequest(ClientEvent<TestRequest> event) {
        var session = event.getSession();
        try {
            sessionService.kickSession(session.getId());
        } catch (Exception e) {

        }
    }

    @EventListener
    public void onNetworkDelayRequest(ClientEvent<NetworkDelayRequest> event) {
        var session = event.getSession();
        try {
            var request = event.getClientRequest();
            UnitySocketService.setNetworkDelay(request.getNetworkDelay(), request.getNetworkBounce());
        } catch (Exception e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}
