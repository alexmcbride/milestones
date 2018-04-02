package wpd2.coursework1.util;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class AntiForgeryHelperTests {
    @Test
    public void testGenerateToken() {
        TestableSession session = new TestableSession();
        AntiForgeryHelper helper = new AntiForgeryHelper(session);

        String token = helper.generateToken();

        assertEquals(session.antiForgeryToken, token);
    }

    @Test
    public void testGenerateTokenCalledMultipleTimes() {
        TestableSession session = new TestableSession();
        AntiForgeryHelper helper = new AntiForgeryHelper(session);

        String token = helper.generateToken();
        token = helper.generateToken();

        assertEquals(session.antiForgeryToken, token);
    }

    @Test(expected = AntiForgeryHelper.AntiForgeryException.class)
    public void testCheckTokenInvalid() {
        TestableSession session = new TestableSession();
        AntiForgeryHelper helper = new AntiForgeryHelper(session);
        String token = helper.generateToken();

        helper.checkToken("invalidToken");
    }

    @Test
    public void testCheckTokenValid() {
        TestableSession session = new TestableSession();
        AntiForgeryHelper helper = new AntiForgeryHelper(session);
        String token = helper.generateToken();

        // Not throwing exception is passing test
        helper.checkToken(token);
    }
}
