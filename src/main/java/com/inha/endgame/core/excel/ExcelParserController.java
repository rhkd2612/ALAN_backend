package com.inha.endgame.core.excel;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ExcelParserController {
    private final ExcelParser excelParser;

    @PostMapping("/parse")
    public String processParsing(@RequestParam String path) {
        String result = "";
        try {
            result = excelParser.convertExcelToJson(path, ".xlsx");
        } catch (Exception e) {
            return e.getMessage();
        }

        return result + "success\n";
    }
}
