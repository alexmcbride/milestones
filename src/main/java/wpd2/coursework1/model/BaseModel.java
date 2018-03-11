package wpd2.coursework1.model;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseModel {
    private Map<String, String> validationErrors;

    public BaseModel() {
        validationErrors = new HashMap<>();
    }

    protected void addValidationError(String key, String message) {
        validationErrors.put(key, message);
    }

    public boolean hasValidationErrors() {
        return validationErrors.size() > 0;
    }

    public boolean hasValidationError(String key) {
        return validationErrors.containsKey(key);
    }

    public String getValidationError(String key) {
        return validationErrors.get(key);
    }

    public boolean isValid() {
        validate();
        return !hasValidationErrors();
    }

    protected abstract void validate();
}
