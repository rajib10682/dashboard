package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SummaryResponseTest {

    private SummaryResponse summaryResponse;

    @BeforeEach
    void setUp() {
        summaryResponse = new SummaryResponse();
    }

    @Test
    void testSummaryResponseCreation() {
        summaryResponse.setTotalPlans(10);
        summaryResponse.setTotalItems(25);
        summaryResponse.setAvgMedianTime(65.5);

        assertEquals(10, summaryResponse.getTotalPlans());
        assertEquals(25, summaryResponse.getTotalItems());
        assertEquals(65.5, summaryResponse.getAvgMedianTime());
    }

    @Test
    void testSummaryResponseEquality() {
        SummaryResponse response1 = new SummaryResponse();
        response1.setTotalPlans(10);
        response1.setTotalItems(25);

        SummaryResponse response2 = new SummaryResponse();
        response2.setTotalPlans(10);
        response2.setTotalItems(25);

        assertEquals(response1.getTotalPlans(), response2.getTotalPlans());
        assertEquals(response1.getTotalItems(), response2.getTotalItems());
    }

    @Test
    void testSummaryResponseToString() {
        summaryResponse.setTotalPlans(10);
        summaryResponse.setTotalItems(25);
        summaryResponse.setAvgMedianTime(65.5);

        String responseString = summaryResponse.toString();
        assertNotNull(responseString);
        assertTrue(responseString.contains("SummaryResponse"));
    }
}
