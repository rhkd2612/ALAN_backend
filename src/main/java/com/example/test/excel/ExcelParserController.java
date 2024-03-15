package com.example.test.excel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExcelParserController {
    private final ExcelParser excelParser;

    // curl -X POST -d "path=/your/file/path" http://your-server-address/parse?path="/data"
    @PostMapping("/parse")
    public String processParsing(@RequestParam String path) {
        try {
            excelParser.convertExcelToJson(path, ".xlsx");
        } catch (Exception e) {
            return e.getMessage();
        }

        return "변환 성공";
    }
}
