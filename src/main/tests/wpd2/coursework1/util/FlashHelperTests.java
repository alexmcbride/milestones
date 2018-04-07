package wpd2.coursework1.util;

import org.junit.Test;
import wpd2.coursework1.helper.FlashHelper;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class FlashHelperTests {
    @Test
    public void testGetMessages() {
        TestableSession session = new TestableSession();
        FlashHelper flashHelper = new FlashHelper(session);

        session.flashMessages = new ArrayList<>();
        session.flashMessages.add(new FlashHelper.FlashMessage("test", "test"));
        assertEquals(session.flashMessages, flashHelper.getMessages());
        assertEquals(1, flashHelper.getMessages().size());

        session.flashMessages = null;
        assertNotNull(flashHelper.getMessages());
        assertEquals(0, flashHelper.getMessages().size());
    }

    @Test
    public void testHasMessages() {
        TestableSession session = new TestableSession();
        FlashHelper flashHelper = new FlashHelper(session);

        session.flashMessages = new ArrayList<>();
        assertTrue(flashHelper.hasMessages());

        session.flashMessages = null;
        assertFalse(flashHelper.hasMessages());
    }

    @Test
    public void testClearMessages() {
        TestableSession session = new TestableSession();
        FlashHelper flashHelper = new FlashHelper(session);

        session.flashMessages = new ArrayList<>();
        session.flashMessages.add(new FlashHelper.FlashMessage("test", "test"));
        flashHelper.clearMessages();

        assertEquals(0, flashHelper.getMessages().size());
    }

    @Test
    public void testMessage() {
        TestableSession session = new TestableSession();
        FlashHelper flashHelper = new FlashHelper(session);
        session.flashMessages = new ArrayList<>();

        flashHelper.message("testContent", FlashHelper.DANGER);

        FlashHelper.FlashMessage result = flashHelper.getMessages().get(0);
        assertEquals("testContent", result.getContent());
        assertEquals(FlashHelper.DANGER, result.getType());
    }

    @Test
    public void testMessageOverload() {
        TestableSession session = new TestableSession();
        FlashHelper flashHelper = new FlashHelper(session);
        session.flashMessages = new ArrayList<>();

        flashHelper.message("testContent");

        FlashHelper.FlashMessage result = flashHelper.getMessages().get(0);
        assertEquals("testContent", result.getContent());
        assertEquals(FlashHelper.SUCCESS, result.getType());
    }
}
