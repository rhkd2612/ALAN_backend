package com.inha.endgame.core.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JsonReader {
    private ExcelParser excelParser;
    private static Map<String, Map<String, Object>> jsons = new ConcurrentHashMap<>();

    public JsonReader(ExcelParser excelParser) throws IOException {
        this.excelParser = excelParser;

        File curDir = this.excelParser.getProjectDirectory();
        File modelDir = new File(curDir.getParent() + "/model");

        this.excelParser.convertExcelToJson(modelDir.getAbsolutePath(), ".xlsx");

        readJsonFilesInFolder(modelDir.getAbsolutePath());
    }

    public static Integer _int(Object value) {
        return (Integer)value;
    }

    public static Integer _time(Object value) {
        return (Integer)value * 1000;
    }

    public static Float _flt(Object value) {
        return ((Number)value).floatValue() / 10000.0f;
    }

    public static String _str(Object value) {
        return (String)value;
    }

    public static Boolean _bool(Object value) {
        return (Boolean)value;
    }

    public static Object model(String fileName, String key, String column) {
        var json = (LinkedHashMap)jsons.get(fileName).get(key);
        return json.get(column);
    }

    public static List<Object> models(String fileName) {
        return new ArrayList<>(jsons.get(fileName).values());
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
