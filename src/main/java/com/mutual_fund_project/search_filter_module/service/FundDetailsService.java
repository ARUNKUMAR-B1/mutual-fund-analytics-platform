package com.mutual_fund_project.search_filter_module.service;

import com.mutual_fund_project.search_filter_module.entity.FundDetails;
import com.mutual_fund_project.search_filter_module.exception.CustomException;
import com.mutual_fund_project.search_filter_module.repository.FundDetailsInterface;
import com.mutual_fund_project.search_filter_module.utilclasses.ParsingExcelFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FundDetailsService {

    @Autowired
    private FundDetailsInterface fundDetailsInterface;

    private final Logger logger = LoggerFactory.getLogger(FundDetailsService.class);

    // Parse Excel and save to DB
    public void parseAndSaveExcel(MultipartFile excelFile) {
        try {
            List<FundDetails> fundList = ParsingExcelFile.parseExcelFile(excelFile);
            fundList.forEach(fundDetailsInterface::save);
        } catch (IOException e) {
            logger.error("Excel parsing failed: {}", e.getMessage(), e);
            throw new CustomException("File parsing error: " + e.getMessage());
        }
    }

    // Get filtered data based on user input
    public List<FundDetails> getData(String name, String risk, String category, String amc) {
        if (name == null && risk == null && category == null && amc == null) {
            logger.warn("No input parameters provided for filtering.");
            throw new CustomException("No data found. Please provide at least one filter.");
        }
        try {
            return fundDetailsInterface.findByFilters(name, risk, category, amc);
        } catch (DataAccessException e) {
            logger.error("Database access error: {}", e.getMessage(), e);
            throw new CustomException("Server issue. Please try again later.");
        }
    }
}
