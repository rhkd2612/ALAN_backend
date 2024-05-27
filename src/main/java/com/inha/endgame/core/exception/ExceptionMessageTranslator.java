package com.inha.endgame.core.exception;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionMessageTranslator {
    private final static String baseError = "선언되지 않은 오류";
    private final static Map<String, String> tempTranslator = new HashMap<>();

    public ExceptionMessageTranslator() {
        tempTranslator.put("NO_FRIEND", "친구가 없습니다.");
    }

    public String translate(Exception e) {
        if(e == null || e.getMessage() == null)
            return baseError;

        String message = e.getMessage();
        if(tempTranslator.containsKey(message))
            return tempTranslator.get(message);

        return message;
    }
}
