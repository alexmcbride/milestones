package wpd2.coursework1.util;

import wpd2.coursework1.model.BaseModel;

public class ValidationHelper {
    public static void required(BaseModel model, String attribute, String value) {
        if (value == null || value.trim().length() == 0) {
            model.addValidationError(attribute, "is required");
        }
    }

    public static void length(BaseModel model, String attribute, String value, int min, int max) {
        if (value == null) {
            model.addValidationError(attribute, String.format("must be between %d and %d characters.", min, max));
        }
        value = value.trim();
        if (value.length() < min || value.length() > max) {
            model.addValidationError(attribute, String.format("must be between %d and %d characters.", min, max));
        }
    }

    public static void email(BaseModel model, String attribute, String value) {
        if (value == null || !isValidEmailAddress(value)) {
            model.addValidationError(attribute, "is not a valid email");
        }
    }

    private static boolean isValidEmailAddress(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static void password(BaseModel model, String attribute, char[] value) {
        if (value == null) {
            model.addValidationError(attribute, "not a valid password");
            return;
        }

        if (value.length < 6) {
            model.addValidationError(attribute, "too short");
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
            model.addValidationError(attribute, "must have at least one alphabetical character");
            return;
        }

        if (!hasNumeric) {
            model.addValidationError(attribute, "must have at least one numeric character");
        }
    }
}
