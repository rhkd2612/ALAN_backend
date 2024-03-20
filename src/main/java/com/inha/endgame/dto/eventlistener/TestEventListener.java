package com.inha.endgame.dto.eventlistener;

import com.inha.endgame.core.io.ClientEvent;
import com.inha.endgame.dto.request.TestRequest;
import com.inha.endgame.dto.response.TestResponse;
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

    @EventListener
    public void onTestRequest(ClientEvent<TestRequest> event) {
        var session = event.getSession();
        try {
            TestRequest request = event.getClientRequest();
            Integer answer = request.getA() + request.getB() + request.getC();
            unitySocketService.sendMessage(session, new TestResponse(answer));
        } catch (IOException e) {
            unitySocketService.sendErrorMessage(session, e);
        }
    }
}
