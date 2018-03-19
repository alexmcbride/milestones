package wpd2.coursework1.util;

import org.junit.Test;
import wpd2.coursework1.model.BaseModel;

import static org.junit.Assert.*;

public class ValidationHelperTests {
    /* Model for help with testing */
    public class TestModel extends BaseModel {
        @Override
        protected void validate() {

        }
    }

    @Test
    public void testRequired() {
        TestModel model = new TestModel();

        ValidationHelper.required(model, "test", "Valid email");
        assertNull(model.getValidationError("test"));

        ValidationHelper.required(model, "test", null);
        assertEquals("is required", model.getValidationError("test"));

        ValidationHelper.required(model, "test", "");
        assertEquals("is required", model.getValidationError("test"));
    }

    @Test
    public void testLength() {
        TestModel model = new TestModel();

        ValidationHelper.length(model, "test", "Valid text", 1, 10);
        assertNull(model.getValidationError("test"));

        ValidationHelper.length(model, "test", "", 1, 5);
        assertEquals("must be between 1 and 5 characters.", model.getValidationError("test"));

        ValidationHelper.length(model, null, "", 1, 5);
        assertEquals("must be between 1 and 5 characters.", model.getValidationError("test"));

        ValidationHelper.length(model, "test", "Invalid text", 1, 5);
        assertEquals("must be between 1 and 5 characters.", model.getValidationError("test"));
    }

    @Test
    public void testEmail() {
        TestModel model = new TestModel();

        ValidationHelper.email(model, "test", "valid@email.com");
        assertNull(model.getValidationError("test"));

        ValidationHelper.email(model, "test", null);
        assertEquals("is not a valid email", model.getValidationError("test"));

        ValidationHelper.email(model, "test", "");
        assertEquals("is not a valid email", model.getValidationError("test"));

        ValidationHelper.email(model, "test", "invalidemail.com");
        assertEquals("is not a valid email", model.getValidationError("test"));

        ValidationHelper.email(model, "test", "invalid@emailcom");
        assertEquals("is not a valid email", model.getValidationError("test"));
    }

    @Test
    public void testPassword() {
        TestModel model = new TestModel();

        ValidationHelper.password(model, "test", "password1".toCharArray());
        assertNull(model.getValidationError("test"));

        ValidationHelper.password(model, "test", null);
        assertEquals("not a valid password", model.getValidationError("test"));

        ValidationHelper.password(model, "test", "".toCharArray());
        assertEquals("too short", model.getValidationError("test"));

        ValidationHelper.password(model, "test", "1234567".toCharArray());
        assertEquals("must have at least one alphabetical character", model.getValidationError("test"));

        ValidationHelper.password(model, "test", "password".toCharArray());
        assertEquals("must have at least one numeric character", model.getValidationError("test"));
    }
}
