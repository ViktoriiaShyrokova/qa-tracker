package org.qatracker.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private boolean valid;
    private List<String> errors;

    private ValidationResult(boolean valid, List<String> errors) {
        this.valid = valid;
        this.errors = errors;
    }

    public static ValidationResult ok() {

        return new ValidationResult(true,new ArrayList<>());
    }
    public static ValidationResult fail(List<String> errors) {

        return new ValidationResult(false,errors);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }
}
