package wpd2.coursework1.model;

import java.util.HashMap;
import java.util.Map;

public abstract class ValidatableModel extends BaseModel {
    private Map<String, String> validationErrors;

    public ValidatableModel() {
        validationErrors = new HashMap<>();
    }

    public boolean hasValidationErrors() {
        return validationErrors.size() > 0;
    }

    public boolean hasValidationError(String attribute) {
        return validationErrors.containsKey(attribute);
    }

    public String getValidationError(String attribute) {
        return validationErrors.get(attribute);
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    // Checks if the child class is valid and returns the result.
    public boolean isValid() {
        validationErrors.clear();
        validate();
        return !hasValidationErrors();
    }

    protected abstract void validate();

    // Adds a validation error for the model.
    public void addValidationError(String attribute, String message) {
        if (message != null) {
            validationErrors.put(attribute, message);
        }
    }
}
