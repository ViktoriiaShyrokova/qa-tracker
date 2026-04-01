package org.qatracker.validation;

import org.qatracker.exception.InvalidTestCaseException;
import org.qatracker.model.TestCase;

import java.util.ArrayList;
import java.util.List;


public class TestCaseValidator {

    public static ValidationResult validate(TestCase tc) {
        List<String> errors = new ArrayList<>();
        if (tc.getTitle() == null ||
                tc.getTitle().isBlank() ||
                tc.getTitle().length() < 3 ||
                tc.getTitle().length() > 200)
            errors.add("Title should have 3-200 symbols");
        if (tc.getPriority() == null || !List.of("LOW", "MEDIUM", "HIGH", "CRITICAL").contains(tc.getPriority()))
            errors.add("Priority should be one of LOW/MEDIUM/HIGH/CRITICAL");
        if(tc.getStatus() == null || !List.of("PENDING","PASSED","FAILED","BLOCKED").contains(tc.getStatus()))
            errors.add("Status should be one of PENDING/PASSED/FAILED/BLOCKED ");
        if(tc.getAssignedTo() != null && tc.getAssignedTo().isBlank())
            errors.add("Assigned to if assigned should not be empty");
        if(!errors.isEmpty()) return ValidationResult.fail(errors);
        return ValidationResult.ok();
    }

    public static void validateOrThrow(TestCase tc) {
        ValidationResult result =  validate(tc);
        if(!result.isValid()) throw new InvalidTestCaseException(result.getErrors());
    }
}