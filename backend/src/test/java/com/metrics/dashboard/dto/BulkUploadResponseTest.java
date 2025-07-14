package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BulkUploadResponseTest {

    private BulkUploadResponse bulkUploadResponse;

    @BeforeEach
    void setUp() {
        bulkUploadResponse = new BulkUploadResponse();
    }

    @Test
    void testBulkUploadResponseCreation() {
        List<String> statusList = Arrays.asList("Success", "Error");
        List<String> reasonList = Arrays.asList("Plan created", "Validation failed");

        bulkUploadResponse.setTotalRecords(2);
        bulkUploadResponse.setSuccessfulRecords(1);
        bulkUploadResponse.setFailedRecords(1);

        assertEquals(2, bulkUploadResponse.getTotalRecords());
        assertEquals(1, bulkUploadResponse.getSuccessfulRecords());
        assertEquals(1, bulkUploadResponse.getFailedRecords());
    }

    @Test
    void testBulkUploadResponseEquality() {
        BulkUploadResponse response1 = new BulkUploadResponse();
        response1.setTotalRecords(10);
        response1.setSuccessfulRecords(8);

        BulkUploadResponse response2 = new BulkUploadResponse();
        response2.setTotalRecords(10);
        response2.setSuccessfulRecords(8);

        assertEquals(response1.getTotalRecords(), response2.getTotalRecords());
        assertEquals(response1.getSuccessfulRecords(), response2.getSuccessfulRecords());
    }

    @Test
    void testBulkUploadResponseToString() {
        bulkUploadResponse.setTotalRecords(100);
        bulkUploadResponse.setSuccessfulRecords(95);
        bulkUploadResponse.setFailedRecords(5);

        String responseString = bulkUploadResponse.toString();
        assertNotNull(responseString);
        assertTrue(responseString.contains("BulkUploadResponse"));
    }
}
