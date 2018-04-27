
package wpd2.coursework1.model;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import wpd2.coursework1.util.*;

import java.sql.Timestamp;
import java.util.Calendar;


import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;


class TempUserTest {
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

    @AfterEach
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

    void testDelete() throws Exception {
        //test user who's account created older than 40 min
        TempUser tempuser = new TempUser();
        tempuser.setUsername("user0");
        tempuser.setEmail("valid@email.com0");
        tempuser.setPassword("password0");
        tempuser.setToken("Token0");
        tempuser.create();
        TempUser tempuserSavedInDatabase = TempUser.findByToken("Token0");
        tempuser.delete();
        assertNull(TempUser.findByToken("Token"));
    }

    @Test
    void testDeleteWithTime() throws Exception {
        //test user who's account created older than 40 min
        TempUser userToBedeleted = new TempUser();
        userToBedeleted.setUsername("user1");
        userToBedeleted.setEmail("valid@email.com1");
        userToBedeleted.setPassword("password1");
        userToBedeleted.setToken("Token1");
        userToBedeleted.create();

        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.MINUTE,-40);
        Timestamp timestamp = new Timestamp(cal1.getTime().getTime());
        TempUser userToBedeletedSavedInDatabase = TempUser.findByToken("Token1");
        userToBedeletedSavedInDatabase.setJoined(timestamp);
        userToBedeletedSavedInDatabase.update();
        userToBedeleted.delete(30);

        assertNull(TempUser.findByToken("Token1"));

        TempUser userOn30minBoundary = new TempUser();
        userOn30minBoundary.setUsername("user2");
        userOn30minBoundary.setEmail("valid@email.com2");
        userOn30minBoundary.setPassword("password2");
        userOn30minBoundary.setToken("Token2");
        userOn30minBoundary.create();
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.MINUTE,-30);
        Timestamp timestamp2 = new Timestamp(cal2.getTime().getTime());
        TempUser userOn30minBoundarySavedInDatabase = TempUser.findByToken("Token2");
        userOn30minBoundarySavedInDatabase.setJoined(timestamp2);
        userOn30minBoundarySavedInDatabase.update();
        userOn30minBoundary.delete(30);

        assertNull(TempUser.findByToken("Token2"));

        TempUser userToBekept = new TempUser();
        userToBekept.setUsername("user3");
        userToBekept.setEmail("valid@email.com3");
        userToBekept.setPassword("password3");
        userToBekept.setToken("Token3");
        userToBekept.create();

        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.MINUTE,-15);
        Timestamp timestamp3 = new Timestamp(cal3.getTime().getTime());
        TempUser userToBekeptSavedInDatabase = TempUser.findByToken("Token3");
        userToBekeptSavedInDatabase.setJoined(timestamp3);
        userToBekeptSavedInDatabase.update();
        userToBekept.delete(30);

        assertNotNull(TempUser.findByToken("Token3"));


    }


}