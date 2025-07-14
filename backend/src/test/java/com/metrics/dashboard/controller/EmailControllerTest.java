package com.metrics.dashboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.metrics.dashboard.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendEmail_WithValidData_ShouldSendEmail() throws Exception {
        Map<String, String> emailRequest = new HashMap<>();
        emailRequest.put("to", "test@example.com");
        emailRequest.put("subject", "Test Subject");
        emailRequest.put("text", "Test message");

        when(emailService.sendNotification(any(Map.class))).thenReturn(Map.of("status", "success"));

        mockMvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isOk());

        verify(emailService).sendNotification(any(Map.class));
    }

    @Test
    void sendEmail_WithMissingTo_ShouldReturnBadRequest() throws Exception {
        Map<String, String> emailRequest = new HashMap<>();
        emailRequest.put("subject", "Test Subject");
        emailRequest.put("text", "Test message");

        mockMvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendEmail_WithEmptySubject_ShouldReturnBadRequest() throws Exception {
        Map<String, String> emailRequest = new HashMap<>();
        emailRequest.put("to", "test@example.com");
        emailRequest.put("subject", "");
        emailRequest.put("text", "Test message");

        mockMvc.perform(post("/api/email/send")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendBulkNotification_WithValidData_ShouldSendNotification() throws Exception {
        Map<String, Object> notificationRequest = new HashMap<>();
        notificationRequest.put("to", "admin@example.com");
        notificationRequest.put("totalRecords", 1000);
        notificationRequest.put("successCount", 950);
        notificationRequest.put("errorCount", 50);

        when(emailService.sendNotification(any(Map.class))).thenReturn(Map.of("status", "success"));

        mockMvc.perform(post("/api/email/bulk-notification")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void sendSystemAlert_WithValidData_ShouldSendAlert() throws Exception {
        Map<String, String> alertRequest = new HashMap<>();
        alertRequest.put("to", "admin@example.com");
        alertRequest.put("alertType", "SYSTEM_ERROR");
        alertRequest.put("message", "Database connection failed");

        when(emailService.sendNotification(any(Map.class))).thenReturn(Map.of("status", "success"));

        mockMvc.perform(post("/api/email/system-alert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(alertRequest)))
                .andExpect(status().isOk());
    }
}
