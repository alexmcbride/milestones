package wpd2.coursework1.helper;

import wpd2.coursework1.model.ValidatableModel;

import java.util.Date;

/*
 * Helper to aid with validating models.
 */
public class ValidationHelper {
    private final ValidatableModel model;

    public ValidationHelper(ValidatableModel model) {
        this.model = model;
    }

    private static String capitalize(String value) {
        if (value.length() > 0) {
            return Character.toUpperCase(value.charAt(0)) + value.substring(1);
        }
        return value;
    }

    public void required(String attribute, String value) {
        if (value == null || value.trim().length() == 0) {
            model.addValidationError(attribute, capitalize(attribute) + " is required");
        }
    }

    public void required(String attribute, Date value) {
        if (value == null || value.getTime() == 0) {
            model.addValidationError(attribute, capitalize(attribute) + " is required");
        }
    }

    public void length(String attribute, String value, int min, int max) {
        if (value == null) {
            model.addValidationError(attribute, String.format("%s must be between %d and %d characters.", capitalize(attribute),  min, max));
            return;
        }
        value = value.trim();
        if (value.length() < min || value.length() > max) {
            model.addValidationError(attribute, String.format("%s must be between %d and %d characters.", capitalize(attribute), min, max));
        }
    }

    public void email(String attribute, String value) {
        if (value == null || !isValidEmailAddress(value)) {
            model.addValidationError(attribute, capitalize(attribute) + " is not a valid email");
        }
    }

    private static boolean isValidEmailAddress(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void password(String attribute, char[] value) {
        if (value == null) {
            model.addValidationError(attribute, capitalize(attribute) + " not a valid password");
            return;
        }

        if (value.length < 6) {
            model.addValidationError(attribute, capitalize(attribute) + " too short");
            return;
        }

        boolean hasAlphabetic = false;
        boolean hasNumeric = false;
        for (char c : value) {
            if (Character.isAlphabetic(c)) {
                hasAlphabetic = true;
            }

            if (Character.isDigit(c)) {
                hasNumeric = true;
            }
        }

        if (!hasAlphabetic) {
            model.addValidationError(attribute, capitalize(attribute) + " must have at least one alphabetical character");
            return;
        }

        if (!hasNumeric) {
            model.addValidationError(attribute, capitalize(attribute) + " must have at least one numeric character");
        }
    }
}
