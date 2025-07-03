package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportController {
    
    @Autowired
    private ExportService exportService;
    
    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportToCsv(@RequestParam(required = false) Integer dataId) {
        try {
            byte[] csvData = exportService.exportToCsv(dataId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "metrics_export.csv");
            return ResponseEntity.ok().headers(headers).body(csvData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
