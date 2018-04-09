package wpd2.coursework1.util;

import org.junit.Test;
import wpd2.coursework1.model.User;

import static junit.framework.TestCase.*;

public class UserManagerTests {
    @Test
    public void testGetUser() {
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);

        session.user = new User();
        assertNotNull(userManager.getUser());
    }

    @Test
    public void testIsLoggedIn() {
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);

        session.user = new User();
        assertTrue(userManager.isLoggedIn());

        session.user = null;
        assertFalse(userManager.isLoggedIn());
    }

    @Test
    public void testGetEmail() {
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);

        session.user = new User();
        session.user.setEmail("valid@email.com");
        assertEquals("valid@email.com", userManager.getEmail());

        session.user = null;
        assertNull(userManager.getEmail());
    }

    @Test
    public void testGetUsername() {
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);

        session.user = new User();
        session.user.setUsername("user1");
        assertEquals("user1", userManager.getUsername());

        session.user = null;
        assertNull(userManager.getUsername());
    }

    @Test
    public void testLogin() {
        User user = new User();
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);
        userManager.login(user);

        assertEquals(user, session.user);
    }

    @Test
    public void testLogout() {
        User user = new User();
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);
        userManager.login(user);

        userManager.logout();

        assertNull(session.user);
    }

    @Test
    public void testGenerateEmailToken() {
        TestableSession session = new TestableSession();
        UserManager userManager = new UserManager(session);

        assertNotNull(userManager.generateEmailToken());
    }
}
