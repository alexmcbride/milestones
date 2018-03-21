package wpd2.coursework1.model;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;

public class UserTests {
    private DatabaseService db;

    @Before
    public void setup() throws SQLException {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        PasswordService pass = new PasswordService(PasswordService.MIN_COST);

        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, pass);

        db.initialize();
    }

    @After
    public void teardown() throws SQLException {
        db.destroy();
    }

    @Test
    public void testValidate() {
        User user = new User();
        user.setUsername("name1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.setJoined(new Date());

        assertTrue(user.isValid());
    }

    @Test
    public void testInvalid() {
        User user = new User();
        assertFalse(user.isValid());

        // TODO: add more tests
    }

    @Test
    public void testCreate() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        user = User.find(user.getId());
        assertEquals(1, user.getId());
        assertEquals("user1", user.getUsername());
        assertEquals("valid@email.com", user.getEmail());
        assertNull(user.getPassword());
        assertNotNull(user.getJoined());
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        user.setUsername("user2");
        user.setEmail("valid2@email.com");
        user.setPassword("password2".toCharArray());
        user.update();

        user = User.find(user.getId());
        assertEquals("user2", user.getUsername());
        assertEquals("valid2@email.com", user.getEmail());
        assertNull(user.getPassword());
        assertNotNull(user.getJoined());
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        user.delete();

        assertNull(User.find(1));
    }

    @Test
    public void testUsernameExists() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        assertTrue(User.usernameExists("user1"));
        assertFalse(User.usernameExists("user2"));
    }

    @Test
    public void testEmailExists() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        assertTrue(User.emailExists("valid@email.com"));
        assertFalse(User.emailExists("invalid@email.com"));
    }

    @Test
    public void testFind() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        assertNotNull(User.find(1));
        assertNotNull(User.find("valid@email.com"));
        assertNull(User.find(2));
        assertNull(User.find("invalid@email.com"));
    }

    @Test
    public void testFindAll() {
        User user = new User();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1".toCharArray());
        user.create();

        user = new User();
        user.setUsername("user2");
        user.setEmail("valid2@email.com");
        user.setPassword("password2".toCharArray());
        user.create();

        List<User> result = User.findAll();
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }
}
