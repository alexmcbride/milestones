package wpd2.coursework1.model;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseModel {
    private Map<String, String> validationErrors;

    public BaseModel() {
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

    // Checks if the child class is valid and returns the result.
    public boolean isValid() {
        validationErrors.clear();
        validate();
        return !hasValidationErrors();
    }

    // Adds a validation error for the model.
    protected void addValidationError(String attribute, String message) {
        validationErrors.put(attribute, message);
    }

    // Child classes implement this method to handle their own validation rules.
    protected abstract void validate();
}
