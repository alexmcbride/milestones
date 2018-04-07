package wpd2.coursework1.model;

import wpd2.coursework1.util.DatabaseService;
import wpd2.coursework1.util.IoC;

import java.sql.Connection;

public abstract class BaseModel {
    protected static Connection getConnection() {
        DatabaseService databaseService = (DatabaseService) IoC.get().getInstance(DatabaseService.class);
        return databaseService.connect();
    }
}
