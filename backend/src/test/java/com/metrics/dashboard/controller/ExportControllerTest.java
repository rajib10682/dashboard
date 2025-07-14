package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.ExportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExportController.class)
class ExportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExportService exportService;

    @Test
    void exportToCSV_WithValidParameters_ShouldReturnCSVFile() throws Exception {
        byte[] csvData = "Plan ID,Plan Name,For Date\n1,Test Plan,2024-03-31".getBytes();
        when(exportService.exportToCsv(anyInt())).thenReturn(csvData);

        mockMvc.perform(get("/api/export/csv")
                .param("forDate", "2024-03-31")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=dashboard_export.csv"));
    }

    @Test
    void exportToExcel_WithValidParameters_ShouldReturnExcelFile() throws Exception {
        byte[] excelData = "Excel file content".getBytes();
        when(exportService.exportToCsv(anyInt())).thenReturn(excelData);

        mockMvc.perform(get("/api/export/excel")
                .param("forDate", "2024-03-31")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().string("Content-Disposition", "attachment; filename=dashboard_export.xlsx"));
    }

    @Test
    void exportToCSV_WithNullForDate_ShouldReturnCSVFile() throws Exception {
        byte[] csvData = "Plan ID,Plan Name,Data ID\n1,Test Plan,1".getBytes();
        when(exportService.exportToCsv(1)).thenReturn(csvData);

        mockMvc.perform(get("/api/export/csv")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "text/csv"));
    }

    @Test
    void exportToExcel_WithNullForDate_ShouldReturnExcelFile() throws Exception {
        byte[] excelData = "Excel file content".getBytes();
        when(exportService.exportToCsv(1)).thenReturn(excelData);

        mockMvc.perform(get("/api/export/excel")
                .param("dataId", "1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
    }

    @Test
    void exportToCSV_WithInvalidDate_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/export/csv")
                .param("forDate", "invalid-date")
                .param("dataId", "1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exportToExcel_WithInvalidDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/export/excel")
                .param("forDate", "2024-03-31")
                .param("dataId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void exportToCSV_WithMissingDataId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/export/csv")
                .param("forDate", "2024-03-31"))
                .andExpect(status().isBadRequest());
    }
}
