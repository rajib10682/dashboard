package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class BulkUploadRequestTest {

    private BulkUploadRequest bulkUploadRequest;

    @BeforeEach
    void setUp() {
        bulkUploadRequest = new BulkUploadRequest();
    }

    @Test
    void testBulkUploadRequestCreation() {
        bulkUploadRequest.setFileName("test.xlsx");
        bulkUploadRequest.setFileData(new byte[]{1, 2, 3});

        assertEquals("test.xlsx", bulkUploadRequest.getFileName());
        assertArrayEquals(new byte[]{1, 2, 3}, bulkUploadRequest.getFileData());
    }

    @Test
    void testBulkUploadRequestEquality() {
        BulkUploadRequest request1 = new BulkUploadRequest();
        request1.setFileName("test.xlsx");
        request1.setFileData(new byte[]{1, 2, 3});

        BulkUploadRequest request2 = new BulkUploadRequest();
        request2.setFileName("test.xlsx");
        request2.setFileData(new byte[]{1, 2, 3});

        assertEquals(request1.getFileName(), request2.getFileName());
        assertArrayEquals(request1.getFileData(), request2.getFileData());
    }

    @Test
    void testBulkUploadRequestToString() {
        bulkUploadRequest.setFileName("test.xlsx");
        bulkUploadRequest.setFileData(new byte[]{1, 2, 3});

        String requestString = bulkUploadRequest.toString();
        assertNotNull(requestString);
        assertTrue(requestString.contains("test.xlsx"));
    }
}
