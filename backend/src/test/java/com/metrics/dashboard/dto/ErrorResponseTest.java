package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        errorResponse = new ErrorResponse();
    }

    @Test
    void testErrorResponseCreation() {
        LocalDateTime timestamp = LocalDateTime.now();
        errorResponse.setTimestamp(timestamp);
        errorResponse.setStatus(400);
        errorResponse.setError("Bad Request");
        errorResponse.setMessage("Validation failed");
        errorResponse.setPath("/api/test");

        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(400, errorResponse.getStatus());
        assertEquals("Bad Request", errorResponse.getError());
        assertEquals("Validation failed", errorResponse.getMessage());
        assertEquals("/api/test", errorResponse.getPath());
    }

    @Test
    void testErrorResponseConstructor() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse constructedResponse = new ErrorResponse(
                404, "Not Found", "Resource not found", "/api/resource");

        assertEquals(timestamp, constructedResponse.getTimestamp());
        assertEquals(404, constructedResponse.getStatus());
        assertEquals("Not Found", constructedResponse.getError());
        assertEquals("Resource not found", constructedResponse.getMessage());
        assertEquals("/api/resource", constructedResponse.getPath());
    }

    @Test
    void testErrorResponseToString() {
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("Database connection failed");

        String responseString = errorResponse.toString();
        assertNotNull(responseString);
        assertTrue(responseString.contains("500"));
        assertTrue(responseString.contains("Internal Server Error"));
    }
}
