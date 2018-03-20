package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

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
    }
}
