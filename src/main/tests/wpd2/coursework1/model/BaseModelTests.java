package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.util.DatabaseService;
import wpd2.coursework1.util.H2DatabaseService;
import wpd2.coursework1.util.IoC;

import java.sql.Connection;
import java.sql.SQLException;

import static junit.framework.TestCase.assertFalse;

public class BaseModelTests {
    private DatabaseService db;

    public class TestBaseModel extends BaseModel {
         public Connection getConnectionForTesting() {
             return getConnection();
         }
    }

    @Before
    public void setup() {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        db.initialize();
    }

    @After
    public void teardown() {
        db.destroy();
    }

    @Test
    public void testGetConnection() throws SQLException {
        TestBaseModel model = new TestBaseModel();
        Connection connection = model.getConnectionForTesting();
        assertFalse(connection.isClosed());
    }
}
