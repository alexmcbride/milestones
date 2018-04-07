package wpd2.coursework1.model;

import org.junit.Test;
import wpd2.coursework1.model.ValidatableModel;

import java.util.Map;

import static junit.framework.TestCase.*;

public class ValidatableModelTests {
    public class TestModel extends ValidatableModel {
        @Override
        protected void validate() {
            addValidationError("test", "test message");
            addValidationError("test2", "test message 2");
        }
    }

    @Test
    public void hasValidationErrorTest() {
        TestModel model = new TestModel();

        assertFalse(model.isValid());
        assertTrue(model.hasValidationError("test"));
    }

    @Test
    public void getValidationErrorsTest() {
        TestModel model = new TestModel();

        assertFalse(model.isValid());
        Map<String, String> result = model.getValidationErrors();
        assertEquals(2, result.size());
        assertEquals("test message", result.get("test"));
        assertEquals("test message 2", result.get("test2"));
    }
}
