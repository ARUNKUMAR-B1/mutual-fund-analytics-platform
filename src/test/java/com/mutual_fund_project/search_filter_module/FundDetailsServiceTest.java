package com.mutual_fund_project.search_filter_module;

import com.mutual_fund_project.search_filter_module.entity.FundDetails;
import com.mutual_fund_project.search_filter_module.exception.CustomException;
import com.mutual_fund_project.search_filter_module.repository.FundDetailsInterface;
import com.mutual_fund_project.search_filter_module.service.FundDetailsService;
import com.mutual_fund_project.search_filter_module.utilclasses.ParsingExcelFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundDetailsServiceTest {

    @Mock
    private FundDetailsInterface fundDetailsInterface;

    @InjectMocks
    private FundDetailsService fundDetailsService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDataWithAllNullsThrowsException() {
        CustomException exception = assertThrows(CustomException.class,
                () -> fundDetailsService.getData(null, null, null, null));
        assertEquals("No data found. Please provide at least one filter.", exception.getMessage());
    }

    @Test
    void testGetDataWithValidInputsReturnsList() {
        List<FundDetails> mockList = Collections.singletonList(new FundDetails());
        when(fundDetailsInterface.findByFilters("Equity", null, null, null)).thenReturn(mockList);

        List<FundDetails> result = fundDetailsService.getData("Equity", null, null, null);
        assertEquals(1, result.size());
    }

    @Test
    void testGetDataThrowsCustomExceptionOnDataAccessException() {
        when(fundDetailsInterface.findByFilters(any(), any(), any(), any()))
                .thenThrow(mock(DataAccessException.class));

        CustomException exception = assertThrows(CustomException.class,
                () -> fundDetailsService.getData("Equity", null, null, null));

        assertEquals("Server issue. Please try again later.", exception.getMessage());
    }

    @Test
    void testParseAndSaveExcelThrowsCustomException() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);

        // Mock static method
        try (MockedStatic<ParsingExcelFile> mockedStatic = mockStatic(ParsingExcelFile.class)) {
            mockedStatic.when(() -> ParsingExcelFile.parseExcelFile(mockFile))
                    .thenThrow(new IOException("IO error"));

            CustomException exception = assertThrows(CustomException.class,
                    () -> fundDetailsService.parseAndSaveExcel(mockFile));

            assertTrue(exception.getMessage().contains("File parsing error"));
        }
    }

    @Test
    void testParseAndSaveExcelSuccess() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        FundDetails fund = new FundDetails();
        List<FundDetails> fundList = Collections.singletonList(fund);

        // Mock static method and verify save
        try (MockedStatic<ParsingExcelFile> mockedStatic = mockStatic(ParsingExcelFile.class)) {
            mockedStatic.when(() -> ParsingExcelFile.parseExcelFile(mockFile))
                    .thenReturn(fundList);

            fundDetailsService.parseAndSaveExcel(mockFile);

            verify(fundDetailsInterface, times(1)).save(fund);
        }
    }
}
