package com.inha.endgame.exception;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ExceptionMessageTranslator {
    private final static String baseError = "예않오";
    private final static Map<String, String> tempTranslator = new HashMap<>();

    public ExceptionMessageTranslator() {
        tempTranslator.put("NO_FRIEND", "친구가 없습니다.");
    }

    public String translate(Exception e) {
        // FIXME 테이블에서 읽어와서 error message를 기획자가 설정한 message로 변경
        if(e == null || e.getMessage() == null)
            return baseError;

        String message = e.getMessage();
        if(tempTranslator.containsKey(message))
            return tempTranslator.get(message);

        return baseError + " : " + message;
    }
}
