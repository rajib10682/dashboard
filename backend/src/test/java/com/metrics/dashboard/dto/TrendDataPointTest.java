package com.metrics.dashboard.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrendDataPointTest {

    private TrendDataPoint trendDataPoint;

    @BeforeEach
    void setUp() {
        trendDataPoint = new TrendDataPoint();
    }

    @Test
    void testTrendDataPointCreation() {
        trendDataPoint.setPeriod("Q1 2024");
        trendDataPoint.setMedianValue(65.5);

        assertEquals("Q1 2024", trendDataPoint.getPeriod());
        assertEquals(65.5, trendDataPoint.getMedianValue());
    }

    @Test
    void testTrendDataPointEquality() {
        TrendDataPoint point1 = new TrendDataPoint();
        point1.setPeriod("Q1 2024");
        point1.setMedianValue(65.5);

        TrendDataPoint point2 = new TrendDataPoint();
        point2.setPeriod("Q1 2024");
        point2.setMedianValue(65.5);

        assertEquals(point1.getPeriod(), point2.getPeriod());
        assertEquals(point1.getMedianValue(), point2.getMedianValue());
    }

    @Test
    void testTrendDataPointToString() {
        trendDataPoint.setPeriod("Q1 2024");
        trendDataPoint.setMedianValue(65.5);

        String pointString = trendDataPoint.toString();
        assertNotNull(pointString);
        assertTrue(pointString.contains("TrendDataPoint"));
    }
}
