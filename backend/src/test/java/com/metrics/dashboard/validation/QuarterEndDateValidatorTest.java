package com.metrics.dashboard.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class QuarterEndDateValidatorTest {

    private QuarterEndDateValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new QuarterEndDateValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_WithValidQ1EndDate_ShouldReturnTrue() {
        LocalDate q1EndDate = LocalDate.of(2024, 3, 31);
        assertTrue(validator.isValid(q1EndDate, context));
    }

    @Test
    void isValid_WithValidQ2EndDate_ShouldReturnTrue() {
        LocalDate q2EndDate = LocalDate.of(2024, 6, 30);
        assertTrue(validator.isValid(q2EndDate, context));
    }

    @Test
    void isValid_WithValidQ3EndDate_ShouldReturnTrue() {
        LocalDate q3EndDate = LocalDate.of(2024, 9, 30);
        assertTrue(validator.isValid(q3EndDate, context));
    }

    @Test
    void isValid_WithValidQ4EndDate_ShouldReturnTrue() {
        LocalDate q4EndDate = LocalDate.of(2024, 12, 31);
        assertTrue(validator.isValid(q4EndDate, context));
    }

    @Test
    void isValid_WithInvalidDate_ShouldReturnFalse() {
        LocalDate invalidDate = LocalDate.of(2024, 3, 15);
        assertFalse(validator.isValid(invalidDate, context));
    }

    @Test
    void isValid_WithMidMonthDate_ShouldReturnFalse() {
        LocalDate midMonthDate = LocalDate.of(2024, 6, 15);
        assertFalse(validator.isValid(midMonthDate, context));
    }

    @Test
    void isValid_WithNullDate_ShouldReturnTrue() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    void isValid_WithLeapYearQ1End_ShouldReturnTrue() {
        LocalDate leapYearQ1 = LocalDate.of(2024, 3, 31);
        assertTrue(validator.isValid(leapYearQ1, context));
    }

    @Test
    void isValid_WithNonLeapYearQ1End_ShouldReturnTrue() {
        LocalDate nonLeapYearQ1 = LocalDate.of(2023, 3, 31);
        assertTrue(validator.isValid(nonLeapYearQ1, context));
    }

    @Test
    void isValid_WithFirstDayOfQuarter_ShouldReturnFalse() {
        LocalDate firstDayQ2 = LocalDate.of(2024, 4, 1);
        assertFalse(validator.isValid(firstDayQ2, context));
    }
}
