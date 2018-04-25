package wpd2.coursework1.model;

import wpd2.coursework1.util.DatabaseService;
import wpd2.coursework1.util.H2DatabaseService;
import wpd2.coursework1.util.IoC;

import java.sql.Connection;

/*
 * Base class that all models inherit from, gives them access to the DB connection.
 */
public abstract class BaseModel {
    /**
     * Gets the current connection with the database.
     *
     * @return the connection.
     */
    protected static Connection getConnection() {
        DatabaseService databaseService = (DatabaseService) IoC.get().getInstance(H2DatabaseService.class);
        return databaseService.connect();
    }
}
