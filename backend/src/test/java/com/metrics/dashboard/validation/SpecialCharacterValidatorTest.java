package com.metrics.dashboard.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SpecialCharacterValidatorTest {

    private SpecialCharacterValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new SpecialCharacterValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void isValid_WithValidString_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("ValidString123"));
    }

    @Test
    void isValid_WithAlphanumericString_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("Test123Plan"));
    }

    @Test
    void isValid_WithSpaces_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("Test Plan Name"));
    }

    @Test
    void isValid_WithHyphens_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("Test-Plan-Name"));
    }

    @Test
    void isValid_WithUnderscores_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("Test_Plan_Name"));
    }

    @Test
    void isValid_WithSpecialCharacters_ShouldReturnFalse() {
        assertTrue(SpecialCharacterValidator.containsSpecialCharacters("Test@Plan"));
    }

    @Test
    void isValid_WithHashSymbol_ShouldReturnFalse() {
        assertTrue(SpecialCharacterValidator.containsSpecialCharacters("Test#Plan"));
    }

    @Test
    void isValid_WithDollarSign_ShouldReturnFalse() {
        assertTrue(SpecialCharacterValidator.containsSpecialCharacters("Test$Plan"));
    }

    @Test
    void isValid_WithPercent_ShouldReturnFalse() {
        assertTrue(SpecialCharacterValidator.containsSpecialCharacters("Test%Plan"));
    }

    @Test
    void isValid_WithNullString_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters(null));
    }

    @Test
    void isValid_WithEmptyString_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters(""));
    }

    @Test
    void isValid_WithOnlyNumbers_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("12345"));
    }

    @Test
    void isValid_WithOnlyLetters_ShouldReturnTrue() {
        assertFalse(SpecialCharacterValidator.containsSpecialCharacters("TestPlan"));
    }
}
