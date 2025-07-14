package com.mutual_fund_project.search_filter_module.controller;

import com.mutual_fund_project.search_filter_module.entity.FundDetails;
import com.mutual_fund_project.search_filter_module.service.FundDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class FundDetailsController {
    @Autowired
    FundDetailsService fundDetailsService;
    private final Logger logger = LoggerFactory.getLogger(FundDetailsController.class);


    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void saveExcelData(@RequestParam("file") MultipartFile excelFile) throws IOException {
         fundDetailsService.parseAndSaveExcel(excelFile);
         logger.info("data is pushed to the database");
    }

    //Getting the details of the fund based on the user inputs
    //implements searchin as well as filtering
    @GetMapping("/get")
    public List<FundDetails> getFundData(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "risk", required = false) String risk,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "amc", required = false) String amc
    ) {
        logger.info("data received from the fronted");
        return fundDetailsService.getData(name, risk, category, amc);
    }

}