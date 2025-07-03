package com.metrics.dashboard.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailService {
    
    public Map<String, Object> sendNotification(Map<String, Object> message) {
        return Map.of(
            "message", "Email notification sent",
            "status", "success",
            "data", message
        );
    }
}
