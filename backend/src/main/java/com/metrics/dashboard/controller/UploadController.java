package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.BulkDataService;
import com.metrics.dashboard.service.ExcelProcessingService;
import com.metrics.dashboard.service.ParallelBulkDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class UploadController {
    
    @Autowired
    private ExcelProcessingService excelProcessingService;
    
    @Autowired
    private BulkDataService bulkDataService;
    
    @Autowired
    private ParallelBulkDataService parallelBulkDataService;
    
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    
    @PostMapping("/bulk")
    public ResponseEntity<byte[]> bulkUpload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (file.getSize() > MAX_FILE_SIZE) {
                return ResponseEntity.badRequest().build();
            }
            
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls"))) {
                return ResponseEntity.badRequest().build();
            }
            
            byte[] fileData = file.getBytes();
            Map<String, List<Map<String, Object>>> excelData = excelProcessingService.parseExcelFile(fileData);
            
            Map<String, List<String>> processingResult = parallelBulkDataService.processParallelBulkData(excelData);
            
            Map<String, List<String>> statusMap = new HashMap<>();
            Map<String, List<String>> reasonMap = new HashMap<>();
            
            int planCount = excelData.get("Plans").size();
            int overrideCount = excelData.get("Overrides").size();
            int itemCount = excelData.get("Items").size();
            
            List<String> allStatuses = processingResult.get("status");
            List<String> allReasons = processingResult.get("reason");
            
            statusMap.put("Plans", allStatuses.subList(0, planCount));
            statusMap.put("Overrides", allStatuses.subList(planCount, planCount + overrideCount));
            statusMap.put("Items", allStatuses.subList(planCount + overrideCount, planCount + overrideCount + itemCount));
            
            reasonMap.put("Plans", allReasons.subList(0, planCount));
            reasonMap.put("Overrides", allReasons.subList(planCount, planCount + overrideCount));
            reasonMap.put("Items", allReasons.subList(planCount + overrideCount, planCount + overrideCount + itemCount));
            
            byte[] responseExcel = excelProcessingService.generateResponseExcel(excelData, statusMap, reasonMap);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "bulk_upload_response_" + System.currentTimeMillis() + ".xlsx");
            
            return ResponseEntity.ok().headers(headers).body(responseExcel);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
