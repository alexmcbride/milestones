package wpd2.coursework1.util;

import java.sql.Connection;

public interface DatabaseService {
    /*
     * Create new connection for required mode.
     */
    Connection connect();

    /*
     * Creates the database tables.
     */
    void initialize();

    /*
     * Destroy the database and drop all its tables.
     */
    void destroy();

    boolean tableExists(String tableName);

    /*
     * Seeds the database with test data.
     */
    void seed();

    public enum Mode {
        DEVELOPMENT,
        TEST
    }
}
