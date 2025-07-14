package com.mutual_fund_project.search_filter_module.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="mutual_fund_data")
public class FundDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fund_ID;
    private String name;
    private String category;
    private String risk;
    private String AMC;
    private String expenseRatio;
    private String yearReturn;
    private String fundSize;

    public Long getFund_ID() {
        return fund_ID;
    }

    public void setFund_ID(Long fund_ID) {
        this.fund_ID = fund_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRisk() {
        return risk;
    }

    public void setRisk(String risk) {
        this.risk = risk;
    }

    public String getAMC() {
        return AMC;
    }

    public void setAMC(String AMC) {
        this.AMC = AMC;
    }

    public String getExpenseRatio() {
        return expenseRatio;
    }

    public void setExpenseRatio(String expenseRatio) {
        this.expenseRatio = expenseRatio;
    }

    public String getYearReturn() {
        return yearReturn;
    }

    public void setYearReturn(String yearReturn) {
        this.yearReturn = yearReturn;
    }

    public String getFundSize() {
        return fundSize;
    }

    public void setFundSize(String fundSize) {
        this.fundSize = fundSize;
    }
}
