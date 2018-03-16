package wpd2.coursework1.model;

import java.sql.Connection;

/*
 * Interface for database connections.
 */
public interface ConnectionFactory {
    Connection build() ;
    void initialize();
    void seed();

    enum Mode {
        DEVELOPMENT,
        TEST
    }
}

