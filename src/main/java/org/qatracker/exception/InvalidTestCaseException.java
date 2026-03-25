package org.qatracker.exception;

import org.qatracker.validation.ValidationResult;

import java.util.List;

public class InvalidTestCaseException extends RuntimeException {

    private List<String> errors;

    public InvalidTestCaseException(List<String> errors) {

        super("Error! TestCase validation failed: " + errors);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
