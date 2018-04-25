package wpd2.coursework1.model;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import wpd2.coursework1.util.*;

import static junit.framework.TestCase.*;

class TempUserTest {
    private DatabaseService db;
    @Before
    void setUp() {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        PasswordService pass = new PasswordServiceImpl(PasswordServiceImpl.MIN_COST);

        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, pass);

        db.initialize();
    }

    @After
    void tearDown()throws Exception {
        db.destroy();
    }

    @Test
    void testCreate()throws Exception {
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
    void testFindByToken()throws Exception {
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
    void testDelete() throws Exception {
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