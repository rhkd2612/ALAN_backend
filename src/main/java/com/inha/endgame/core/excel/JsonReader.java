package com.inha.endgame.core.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JsonReader {
    private static Map<String, Map<String, Object>> jsons = new ConcurrentHashMap<>();

    public JsonReader() {
        //readJsonFilesInFolder("/data");
        log.info("success");
    }

    public static Object getModel(String fileName, String key) {
        return jsons.get(fileName).get(key);
    }

    private static void readJsonFilesInFolder(String folderPath) {
        ObjectMapper objectMapper = new ObjectMapper();

        var folder = new File(folderPath);
        if (!folder.isDirectory())
            throw new IllegalArgumentException("The provided path is not a directory.");

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (files != null) {
            for (File file : files) {
                try {
                    // JSON 파일 읽어오기
                    List<LinkedHashMap> rows = objectMapper.readValue(file, List.class);
                    Map<String, Object> dataMap = new HashMap<>();
                    rows.forEach(row -> {
                        var id = row.get("id").toString();
                        dataMap.put(id, row);
                    });

                    jsons.put(file.getName().substring(0, file.getName().lastIndexOf('.')), dataMap);
                } catch (IOException e) {
                    System.err.println("Error reading JSON file: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }
}
