package com.metrics.dashboard.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithValidParameters_ShouldSendEmail() {
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test message body";

        Map<String, Object> result = emailService.sendNotification(Map.of("to", to, "subject", subject, "text", text));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendEmail_WithNullTo_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendNotification(Map.of("to", (String)null, "subject", "Subject", "text", "Text"));
        });
    }

    @Test
    void sendEmail_WithEmptyTo_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendNotification(Map.of("to", "", "subject", "Subject", "text", "Text"));
        });
    }

    @Test
    void sendEmail_WithNullSubject_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendNotification(Map.of("to", "test@example.com", "subject", (String)null, "text", "Text"));
        });
    }

    @Test
    void sendEmail_WithNullText_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendNotification(Map.of("to", "test@example.com", "subject", "Subject", "text", (String)null));
        });
    }

    @Test
    void sendBulkProcessingNotification_WithValidParameters_ShouldSendEmail() {
        String to = "admin@example.com";
        int totalRecords = 1000;
        int successCount = 950;
        int errorCount = 50;

        Map<String, Object> result = emailService.sendNotification(Map.of(
            "to", to, 
            "subject", "Bulk Processing Complete", 
            "totalRecords", totalRecords,
            "successCount", successCount,
            "errorCount", errorCount
        ));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendBulkProcessingNotification_WithZeroRecords_ShouldSendEmail() {
        String to = "admin@example.com";
        int totalRecords = 0;
        int successCount = 0;
        int errorCount = 0;

        Map<String, Object> result = emailService.sendNotification(Map.of(
            "to", to, 
            "subject", "Bulk Processing Complete", 
            "totalRecords", totalRecords,
            "successCount", successCount,
            "errorCount", errorCount
        ));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendSystemAlert_WithValidParameters_ShouldSendEmail() {
        String to = "admin@example.com";
        String alertType = "SYSTEM_ERROR";
        String message = "Database connection failed";

        Map<String, Object> result = emailService.sendNotification(Map.of(
            "to", to, 
            "alertType", alertType, 
            "message", message
        ));

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendSystemAlert_WithNullAlertType_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendNotification(Map.of(
                "to", "admin@example.com", 
                "alertType", (String)null, 
                "message", "Message"
            ));
        });
    }

    @Test
    void sendEmail_WhenMailSenderThrowsException_ShouldPropagateException() {
        doThrow(new RuntimeException("Mail server error")).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(RuntimeException.class, () -> {
            emailService.sendNotification(Map.of("to", "test@example.com", "subject", "Subject", "text", "Text"));
        });
    }
}
