package com.metrics.dashboard.controller;

import com.metrics.dashboard.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send")
    public ResponseEntity<Map<String, Object>> sendNotification(@RequestBody Map<String, Object> message) {
        Map<String, Object> result = emailService.sendNotification(message);
        return ResponseEntity.ok(result);
    }
}
