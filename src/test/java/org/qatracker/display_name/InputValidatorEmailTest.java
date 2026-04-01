package org.qatracker.display_name;

import org.qatracker.validation.InputValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Input Validator - email validator")
public class InputValidatorEmailTest {

    @Test
    @DisplayName("Valid email with standard format should pass")
    void validStandardEmail() {
        assertTrue(InputValidator.isValidEmail("user@example.com"));
    }

    @Test
    @DisplayName("Email without @ symbol should be rejected")
    void emailWithoutAtSign() {
        assertFalse(InputValidator.isValidEmail("userexample.com"));
    }
}
