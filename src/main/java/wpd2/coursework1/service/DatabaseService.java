package wpd2.coursework1.service;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * Interface for database connection service, which creates the connection, initializes the database, and seeds data.
 */
public interface DatabaseService {
    enum Mode {
        DEVELOPMENT,
        TEST
    }

    Connection connect() ;
    void initialize() throws SQLException;
    void seed() throws SQLException;
    void destroy() throws SQLException;
}


