package wpd2.coursework1.model;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import wpd2.coursework1.util.*;

import static junit.framework.TestCase.*;

public class TempUserTest {
    private DatabaseService db;
    private TestableEmailService email;

    class TestableEmailService implements EmailService {
        @Override
        public boolean SendEmailUsingGMailSMTP(String email, String Sbj, String Msg) {
            return true;
        }
    }

    @Before
    public void setUp() {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        PasswordService pass = new PasswordServiceImpl(PasswordServiceImpl.MIN_COST);
        email = new TestableEmailService();

        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, pass);
        container.registerInstance(EmailService.class, email);

        db.initialize();
    }

    @After
    public void tearDown()throws Exception {
        db.destroy();
    }

    @Test
    public void testCreate()throws Exception {
        TempUser user = new TempUser();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1");
        user.setToken("Token");
        user.create();
        TempUser userSavedInDB = TempUser.findByToken("Token");
        assertEquals(1, userSavedInDB.getId());
        assertEquals("user1", userSavedInDB.getUsername());
        assertEquals("valid@email.com", userSavedInDB.getEmail());
        assertNotNull(userSavedInDB.getPasswordHash());
        assertNull(userSavedInDB.getPassword());
        assertEquals("Token", userSavedInDB.getToken());
        assertNotNull(userSavedInDB.getJoined());
    }

    @Test
    public void testFindByToken()throws Exception {
        TempUser user = new TempUser();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1");
        user.setToken("Token");
        user.create();
        TempUser userSavedInDB = TempUser.findByToken("Token");
        assertNotNull(userSavedInDB);
    }


    @Test
    public void testDelete() throws Exception {
        TempUser user = new TempUser();
        user.setUsername("user1");
        user.setEmail("valid@email.com");
        user.setPassword("password1");
        user.setToken("Token");
        user.create();

        user.delete();

        assertNull(TempUser.findByToken("Token"));
    }
}