package com.inha.endgame.excel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelParser {
    private final ObjectMapper objectMapper;

    @PostConstruct
    void init() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void convertExcelToJson(String folderPath, String extension) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));

        if (files != null) {
            for (File file : files) {
                List<List<String>> excelData = readExcel(file);
                String json = convertToJson(excelData);
                saveJsonFile(file.getPath().replace(".xlsx", ".json"), json);
            }
        }
    }

    private List<List<String>> readExcel(File file) throws IOException {
        List<List<String>> excelData = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    rowData.add(cell.toString());
                }
                excelData.add(rowData);
            }
        }
        return excelData;
    }

    private String convertToJson(List<List<String>> excelData) {
        var columns = excelData.get(0);
        var types = excelData.get(2);

        var jsonArray = objectMapper.createArrayNode();
        for(int i = 3; i < excelData.size(); i++) {
            var jsonObject = objectMapper.createObjectNode();
            var row = excelData.get(i);

            for(int j = 0; j < row.size(); j++) {
                var type = types.get(j);
                var value = row.get(j);

                if(value == null) {
                    throw new IllegalStateException(i + 1 + "라인 " + j + 1 + "번째가 null입니다.(null을 허용하지 않음)");
                } else {
                    var columnName = columns.get(j);
                    if (type.equals("int")) {
                        jsonObject.put(columnName, new BigInteger(value.replaceAll("\\.\\d+$", "")));
                    } else if (type.equals("bool")) {
                        jsonObject.put(columnName, value.equals("TRUE"));
                    } else {
                        // string
                        jsonObject.put(columnName, value);
                    }
                }
            }

            jsonArray.add(jsonObject);
        }

        return jsonArray.toPrettyString();
    }

    private void saveJsonFile(String fileName, String json) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(json.getBytes());
        }
    }
}
