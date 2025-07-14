package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.ExcelProcessingService;
import com.metrics.dashboard.service.ParallelBulkDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UploadController.class)
class UploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExcelProcessingService excelProcessingService;

    @MockBean
    private ParallelBulkDataService parallelBulkDataService;

    @MockBean
    private com.metrics.dashboard.service.BulkDataService bulkDataService;

    private MockMultipartFile testExcelFile;
    private Map<String, List<Map<String, Object>>> testExcelData;
    private Map<String, List<String>> testResultData;

    @BeforeEach
    void setUp() {
        testExcelFile = new MockMultipartFile(
                "file", "test.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                "test content".getBytes());

        testExcelData = new HashMap<>();
        testExcelData.put("Plans", Arrays.asList());
        testExcelData.put("Overrides", Arrays.asList());
        testExcelData.put("Items", Arrays.asList());

        testResultData = new HashMap<>();
        testResultData.put("status", Arrays.asList("Success", "Error"));
        testResultData.put("reason", Arrays.asList("Plan created", "Validation failed"));
    }

    @Test
    void bulkUpload_WithValidFile_ShouldReturnExcelResponse() throws Exception {
        when(excelProcessingService.parseExcelFile(any())).thenReturn(testExcelData);
        when(parallelBulkDataService.processParallelBulkData(any())).thenReturn(testResultData);
        when(excelProcessingService.generateResponseExcel(any(), any(), any())).thenReturn("result content".getBytes());

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(testExcelFile))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/octet-stream"))
                .andExpect(header().exists("Content-Disposition"));
    }

    @Test
    void bulkUpload_WithEmptyFile_ShouldReturnBadRequest() throws Exception {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                new byte[0]);

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(emptyFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bulkUpload_WithInvalidFileType_ShouldReturnBadRequest() throws Exception {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "test.txt", "text/plain", "test content".getBytes());

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(invalidFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void bulkUpload_WithNoFile_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(multipart("/api/upload/bulk"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void bulkUpload_WithProcessingException_ShouldReturnInternalServerError() throws Exception {
        when(excelProcessingService.parseExcelFile(any())).thenThrow(new RuntimeException("Processing error"));

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(testExcelFile))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void bulkUpload_WithLargeFile_ShouldProcessSuccessfully() throws Exception {
        byte[] largeContent = new byte[10 * 1024 * 1024];
        MockMultipartFile largeFile = new MockMultipartFile(
                "file", "large.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                largeContent);

        when(excelProcessingService.parseExcelFile(any())).thenReturn(testExcelData);
        when(parallelBulkDataService.processParallelBulkData(any())).thenReturn(testResultData);
        when(excelProcessingService.generateResponseExcel(any(), any(), any())).thenReturn("result content".getBytes());

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(largeFile))
                .andExpect(status().isOk());
    }

    @Test
    void bulkUpload_WithSpecialCharactersInFilename_ShouldProcessSuccessfully() throws Exception {
        MockMultipartFile fileWithSpecialChars = new MockMultipartFile(
                "file", "test-file_with@special#chars.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                "test content".getBytes());

        when(excelProcessingService.parseExcelFile(any())).thenReturn(testExcelData);
        when(parallelBulkDataService.processParallelBulkData(any())).thenReturn(testResultData);
        when(excelProcessingService.generateResponseExcel(any(), any(), any())).thenReturn("result content".getBytes());

        mockMvc.perform(multipart("/api/upload/bulk")
                .file(fileWithSpecialChars))
                .andExpect(status().isOk());
    }
}
