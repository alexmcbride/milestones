package wpd2.coursework1.service;

import java.sql.Connection;

/*
 * Interface for database connection service, which creates the connection, initializes the database, and seeds data.
 */
public interface ConnectionService {
    enum Mode {
        DEVELOPMENT,
        TEST
    }

    Connection connect() ;
    void initialize();
    void seed();
}


