package com.mutual_fund_project.search_filter_module.repository;

import com.mutual_fund_project.search_filter_module.entity.FundDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FundDetailsInterface extends JpaRepository<FundDetails, Long> {

    @Query("SELECT f FROM FundDetails f " +
            "WHERE (:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:risk IS NULL OR LOWER(f.risk) LIKE LOWER(CONCAT('%', :risk, '%'))) " +
            "AND (:category IS NULL OR LOWER(f.category) LIKE LOWER(CONCAT('%', :category, '%'))) " +
            "AND (:amc IS NULL OR LOWER(f.AMC) LIKE LOWER(CONCAT('%', :amc, '%')))")
    List<FundDetails> findByFilters(
            @Param("name") String name,
            @Param("risk") String risk,
            @Param("category") String category,
            @Param("amc") String amc
    );
}
