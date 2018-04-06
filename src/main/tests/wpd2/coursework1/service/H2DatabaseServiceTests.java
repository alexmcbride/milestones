package wpd2.coursework1.service;

import org.junit.Test;
import wpd2.coursework1.util.H2DatabaseService;
import wpd2.coursework1.util.IoC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

public class H2DatabaseServiceTests {
    @Test
    public void testConnect() throws SQLException {
        H2DatabaseService db = new H2DatabaseService(H2DatabaseService.Mode.TEST);
        try (Connection conn = db.connect()) {
            assertFalse(conn.isClosed());
        }
    }

    private boolean checkTableExists(H2DatabaseService db, String tableName) throws SQLException {
        try (Connection conn = db.connect()) {
            ResultSet result = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null);
            return result.next();
        }
    }

    @Test
    public void testInitialize() throws SQLException {
        H2DatabaseService db = new H2DatabaseService(H2DatabaseService.Mode.TEST);
        db.initialize();
        assertTrue(checkTableExists(db, "projects"));
    }

    @Test
    public void testDestroy() throws SQLException {
        H2DatabaseService db = new H2DatabaseService(H2DatabaseService.Mode.TEST);
        db.initialize();
        db.destroy();
        assertFalse(checkTableExists(db, "projects"));
    }

    @Test
    public void testSeed() throws SQLException {
        H2DatabaseService db = new H2DatabaseService(H2DatabaseService.Mode.TEST);
        IoC.get().registerInstance(H2DatabaseService.class, db);
        db.initialize();
        db.seed();

        int count = 0;
        try (Connection conn = db.connect(); Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery("SELECT COUNT(*) FROM projects");
            if (result.next()) {
                count = result.getInt(1);
            }
        }
        assertEquals(count, 10);
    }
}

