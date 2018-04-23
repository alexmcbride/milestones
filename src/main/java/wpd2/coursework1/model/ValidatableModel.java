package wpd2.coursework1.model;

import java.util.HashMap;
import java.util.Map;

/*
 * Base class for implementing validation for a model, children override the validate() model and add validation errors
 * to parent.
 */
public abstract class ValidatableModel extends BaseModel {
    private Map<String, String> validationErrors;

    /**
     * Creates a new ValidatableModel.
     */
    public ValidatableModel() {
        validationErrors = new HashMap<>();
    }

    /**
     * Checks if there are any validation errors in the model.
     *
     * @return true if the model has validation errors.
     */
    public boolean hasValidationErrors() {
        return validationErrors.size() > 0;
    }

    /**
     * Checks if there is a validation error for the attribute
     *
     * @param attribute the attribute to check for errors
     * @return true if the attribute has an error associated with it
     */
    public boolean hasValidationError(String attribute) {
        return validationErrors.containsKey(attribute);
    }

    /**
     * Gets error for single attribute
     *
     * @param attribute the attribute to get the error for
     * @return error string associated with an attribute
     */
    public String getValidationError(String attribute) {
        return validationErrors.get(attribute);
    }

    /**
     * Gets all the validation errors in the model.
     *
     * @return map of errors
     */
    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    /**
     * Removes all validation errors in the mode.
     */
    public void clearValidationErrors() {
        validationErrors.clear();
    }

    /**
     * Checks if the child class is valid and returns the result.
     *
     * @return true if the model is valid.
     */
    public boolean isValid() {
        validate();
        return !hasValidationErrors();
    }

    /**
     * Abstract method that child classes implement in order to provide their own validation rules.
     */
    protected abstract void validate();

    /**
     * Adds a validation error to the model.
     *
     * @param attribute the attribute to add the error for
     * @param message the message for the error
     */
    public void addValidationError(String attribute, String message) {
        if (message != null) {
            validationErrors.put(attribute, message);
        }
    }
}
