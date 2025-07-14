package com.metrics.dashboard.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelProcessingServiceTest {

    @InjectMocks
    private ExcelProcessingService excelProcessingService;

    private MockMultipartFile testExcelFile;
    private Map<String, List<String>> testResultData;

    @BeforeEach
    void setUp() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        
        Sheet planSheet = workbook.createSheet("Plans");
        Row planHeader = planSheet.createRow(0);
        planHeader.createCell(0).setCellValue("Plan_Name");
        planHeader.createCell(1).setCellValue("For_Date");
        planHeader.createCell(2).setCellValue("Data_ID");
        
        Row planData = planSheet.createRow(1);
        planData.createCell(0).setCellValue("Test Plan");
        planData.createCell(1).setCellValue("31-Mar-2024");
        planData.createCell(2).setCellValue("1");

        Sheet overrideSheet = workbook.createSheet("Overrides");
        Row overrideHeader = overrideSheet.createRow(0);
        overrideHeader.createCell(0).setCellValue("Plan_Name");
        overrideHeader.createCell(1).setCellValue("Override_Name");
        overrideHeader.createCell(2).setCellValue("Total_Execution_Time");
        overrideHeader.createCell(3).setCellValue("On_Hold_Time");
        overrideHeader.createCell(4).setCellValue("Core_Execution_Time");
        overrideHeader.createCell(5).setCellValue("Request_Type");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        testExcelFile = new MockMultipartFile(
                "file", "test.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                baos.toByteArray());

        testResultData = new HashMap<>();
        testResultData.put("status", Arrays.asList("Success", "Error"));
        testResultData.put("reason", Arrays.asList("Plan created", "Validation failed"));
    }

    @Test
    void parseExcelFile_WithValidFile_ShouldReturnParsedData() throws IOException {
        Map<String, List<Map<String, Object>>> result = excelProcessingService.parseExcelFile(testExcelFile.getBytes());

        assertNotNull(result);
        assertTrue(result.containsKey("Plans"));
        assertTrue(result.containsKey("Overrides"));
        
        List<Map<String, Object>> plans = result.get("Plans");
        assertEquals(1, plans.size());
        assertEquals("Test Plan", plans.get(0).get("Plan_Name"));
        assertEquals("31-Mar-2024", plans.get(0).get("For_Date"));
        assertEquals("1", plans.get(0).get("Data_ID"));
    }

    @Test
    void parseExcelFile_WithEmptyFile_ShouldReturnEmptyData() throws IOException {
        Workbook emptyWorkbook = new XSSFWorkbook();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        emptyWorkbook.write(baos);
        emptyWorkbook.close();

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.xlsx", 
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", 
                baos.toByteArray());

        Map<String, List<Map<String, Object>>> result = excelProcessingService.parseExcelFile(emptyFile.getBytes());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void parseExcelFile_WithInvalidFile_ShouldThrowException() {
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file", "invalid.txt", "text/plain", "invalid content".getBytes());

        assertThrows(Exception.class, () -> {
            excelProcessingService.parseExcelFile(invalidFile.getBytes());
        });
    }

    @Test
    void generateResultExcel_WithValidData_ShouldCreateExcelFile() throws IOException {
        Map<String, List<Map<String, Object>>> originalData = new HashMap<>();
        originalData.put("Plans", Arrays.asList(Map.of("Plan_Name", "Test Plan", "For_Date", "31-Mar-2024")));
        originalData.put("Overrides", new ArrayList<>());
        originalData.put("Items", new ArrayList<>());
        
        Map<String, List<String>> statusData = new HashMap<>();
        statusData.put("Plans", Arrays.asList("Success"));
        statusData.put("Overrides", new ArrayList<>());
        statusData.put("Items", new ArrayList<>());
        
        Map<String, List<String>> reasonData = new HashMap<>();
        reasonData.put("Plans", Arrays.asList("Plan created"));
        reasonData.put("Overrides", new ArrayList<>());
        reasonData.put("Items", new ArrayList<>());
        
        byte[] result = excelProcessingService.generateResponseExcel(originalData, statusData, reasonData);

        assertNotNull(result);
        assertTrue(result.length > 0);

        Workbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(result));
        Sheet sheet = workbook.getSheet("Plans");
        assertNotNull(sheet);
        
        Row headerRow = sheet.getRow(0);
        assertEquals("Plan_Name", headerRow.getCell(0).getStringCellValue());
        assertEquals("Status", headerRow.getCell(headerRow.getLastCellNum() - 2).getStringCellValue());
        assertEquals("Reason", headerRow.getCell(headerRow.getLastCellNum() - 1).getStringCellValue());
        
        Row dataRow1 = sheet.getRow(1);
        assertEquals("Success", dataRow1.getCell(headerRow.getLastCellNum() - 2).getStringCellValue());
        assertEquals("Plan created", dataRow1.getCell(headerRow.getLastCellNum() - 1).getStringCellValue());
        
        workbook.close();
    }

    @Test
    void generateResultExcel_WithEmptyData_ShouldCreateEmptyExcelFile() throws IOException {
        Map<String, List<Map<String, Object>>> originalData = new HashMap<>();
        originalData.put("Plans", new ArrayList<>());
        originalData.put("Overrides", new ArrayList<>());
        originalData.put("Items", new ArrayList<>());
        
        Map<String, List<String>> statusData = new HashMap<>();
        statusData.put("Plans", new ArrayList<>());
        statusData.put("Overrides", new ArrayList<>());
        statusData.put("Items", new ArrayList<>());
        
        Map<String, List<String>> reasonData = new HashMap<>();
        reasonData.put("Plans", new ArrayList<>());
        reasonData.put("Overrides", new ArrayList<>());
        reasonData.put("Items", new ArrayList<>());

        byte[] result = excelProcessingService.generateResponseExcel(originalData, statusData, reasonData);

        assertNotNull(result);
        assertTrue(result.length > 0);

        Workbook workbook = new XSSFWorkbook(new java.io.ByteArrayInputStream(result));
        Sheet sheet = workbook.getSheet("Plans");
        assertNotNull(sheet);
        
        Row headerRow = sheet.getRow(0);
        assertEquals("Plan_Name", headerRow.getCell(0).getStringCellValue());
        assertEquals("Status", headerRow.getCell(headerRow.getLastCellNum() - 2).getStringCellValue());
        assertEquals("Reason", headerRow.getCell(headerRow.getLastCellNum() - 1).getStringCellValue());
        
        assertEquals(0, sheet.getLastRowNum());
        
        workbook.close();
    }

    @Test
    void generateResultExcel_WithNullData_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            excelProcessingService.generateResponseExcel(null, null, null);
        });
    }
}
