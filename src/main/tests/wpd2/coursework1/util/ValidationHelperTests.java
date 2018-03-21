package wpd2.coursework1.util;

import org.junit.Test;
import org.junit.Before;
import wpd2.coursework1.model.ValidatableModel;

import static org.junit.Assert.*;

public class ValidationHelperTests {
    private TestModel model;
    private ValidationHelper helper;

    /* Model for help with testing */
    public class TestModel extends ValidatableModel {
        @Override
        protected void validate() {

        }
    }

    @Before
    public void setup() {
        model = new TestModel();
        helper = new ValidationHelper(model);
    }

    @Test
    public void testRequired() {
        helper.required("test", "Valid email");
        assertNull(model.getValidationError("test"));

        helper.required("test", (String)null);
        assertEquals("Test is required", model.getValidationError("test"));

        helper.required("test", "");
        assertEquals("Test is required", model.getValidationError("test"));

//        helper.required("test", new Date());
//        assertNull(model.getValidationError("test"));
//
//        helper.required("test", new Date(0));
//        assertEquals("Test is required", model.getValidationError("test"));
    }

    @Test
    public void testLength() {
        helper.length("test", "Valid text", 1, 10);
        assertNull(model.getValidationError("test"));

        helper.length("test", "", 1, 5);
        assertEquals("Test must be between 1 and 5 characters.", model.getValidationError("test"));

        helper.length("test", null, 1, 5);
        assertEquals("Test must be between 1 and 5 characters.", model.getValidationError("test"));

        helper.length("test", "Invalid text", 1, 5);
        assertEquals("Test must be between 1 and 5 characters.", model.getValidationError("test"));
    }

    @Test
    public void testEmail() {
        helper.email("test", "valid@email.com");
        assertNull(model.getValidationError("test"));

        helper.email("test", null);
        assertEquals("Test is not a valid email", model.getValidationError("test"));

        helper.email("test", "");
        assertEquals("Test is not a valid email", model.getValidationError("test"));

        helper.email("test", "invalidemail.com");
        assertEquals("Test is not a valid email", model.getValidationError("test"));

        helper.email("test", "invalid@emailcom");
        assertEquals("Test is not a valid email", model.getValidationError("test"));
    }

    @Test
    public void testPassword() {
        helper.password("test", "password1".toCharArray());
        assertNull(model.getValidationError("test"));

        helper.password("test", null);
        assertEquals("Test not a valid password", model.getValidationError("test"));

        helper.password("test", "".toCharArray());
        assertEquals("Test too short", model.getValidationError("test"));

        helper.password("test", "1234567".toCharArray());
        assertEquals("Test must have at least one alphabetical character", model.getValidationError("test"));

        helper.password("test", "password".toCharArray());
        assertEquals("Test must have at least one numeric character", model.getValidationError("test"));
    }
}
