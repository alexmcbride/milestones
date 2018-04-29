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

    /**
     * Checks if a table exists in the database.
     *
     * @param tableName the name of the true
     * @return true if it exists
     */
    boolean tableExists(String tableName);

    /*
     * Seeds the database with test data.
     */
    void seed();

    /**
     * Mode to start the DB in.
     */
    enum Mode {
        /**
         * In development use file DB
         */
        DEVELOPMENT,

        /**
         * In tests use in memory DB
         */
        TEST
    }
}
