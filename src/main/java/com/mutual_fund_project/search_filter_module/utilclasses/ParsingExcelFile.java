package com.mutual_fund_project.search_filter_module.utilclasses;

import com.mutual_fund_project.search_filter_module.entity.FundDetails;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ParsingExcelFile {

    // Parses the Excel file and returns a list of FundDetails
    public static List<FundDetails> parseExcelFile(MultipartFile excelFile) throws IOException {
        List<FundDetails> fundList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(excelFile.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                FundDetails fund = new FundDetails();
                fund.setName(getCellAsString(row.getCell(0)));
                fund.setCategory(getCellAsString(row.getCell(1)));
                fund.setRisk(getCellAsString(row.getCell(2)));
                fund.setAMC(getCellAsString(row.getCell(3)));
                fund.setYearReturn(getCellAsString(row.getCell(4)));
                fund.setExpenseRatio(getCellAsString(row.getCell(5)));
                fund.setFundSize(getCellAsString(row.getCell(6)));

                fundList.add(fund);
            }
        }
        return fundList;
    }

    private static String getCellAsString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                }
                double val = cell.getNumericCellValue();
                yield (val == Math.floor(val)) ? String.valueOf((long) val) : String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> {
                try {
                    yield cell.getStringCellValue();
                } catch (Exception e) {
                    yield String.valueOf(cell.getNumericCellValue());
                }
            }
            default -> "";
        };
    }
}
