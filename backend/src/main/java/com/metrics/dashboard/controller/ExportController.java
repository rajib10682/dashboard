package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/export")
public class ExportController {
    
    @Autowired
    private ExportService exportService;
    
    @GetMapping("/csv")
    public ResponseEntity<String> exportToCsv(@RequestParam(required = false) Integer dataId) {
        try {
            Map<String, String> result = exportService.exportToCsv(dataId);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", result.get("filename"));
            
            return ResponseEntity.ok()
                .headers(headers)
                .body(result.get("csvData"));
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
