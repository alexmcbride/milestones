package wpd2.coursework1.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ValidationErrorTests {
    @Test
    public void testRequired() {
        assertNull(ValidationError.required("Valid email"));
        assertEquals("is required", ValidationError.required(null));
        assertEquals("is required", ValidationError.required(""));
    }

    @Test
    public void testLength() {
        assertNull(ValidationError.length("Valid text", 1, 10));
        assertEquals("must be between 1 and 5 characters.", ValidationError.length("", 1, 5));
        assertEquals("must be between 1 and 5 characters.", ValidationError.length(null, 1, 5));
        assertEquals("must be between 1 and 5 characters.", ValidationError.length("Invalid text", 1, 5));
    }

    @Test
    public void testEmail() {
        assertNull(ValidationError.email("valid@email.com"));
        assertEquals("is not a valid email", ValidationError.email(null));
        assertEquals("is not a valid email", ValidationError.email(""));
        assertEquals("is not a valid email", ValidationError.email("invalidemail.com"));
        assertEquals("is not a valid email", ValidationError.email("invalid@emailcom"));
    }

    @Test
    public void testPassword() {
        assertNull(ValidationError.password("password1".toCharArray()));
        assertEquals("not a valid password", ValidationError.password(null));
        assertEquals("too short", ValidationError.password("".toCharArray()));
        assertEquals("must have at least one alphabetical character", ValidationError.password("1234567".toCharArray()));
        assertEquals("must have at least one numeric character", ValidationError.password("password".toCharArray()));
    }
}
