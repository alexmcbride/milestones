package wpd2.coursework1.util;

public class ValidationError {
    public static String required(String value) {
        if (value == null || value.trim().length() == 0) {
            return "is required";
        }
        return null;
    }

    public static String length(String value, int min, int max) {
        if (value == null) {
            return String.format("must be between %d and %d characters.", min, max);
        }
        value = value.trim();
        if (value.length() < min || value.length() > max) {
            return String.format("must be between %d and %d characters.", min, max);
        }
        return null;
    }

    public static String email(String value) {
        if (value == null || !isValidEmailAddress(value)) {
            return "is not a valid email";
        }
        return null;
    }

    private static boolean isValidEmailAddress(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static String password(char[] value) {
        if (value == null) {
            return "not a valid password";
        }

        if (value.length < 6) {
            return "too short";
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
            return "must have at least one alphabetical character";
        }

        if (!hasNumeric) {
            return "must have at least one numeric character";
        }

        return null;
    }
}
