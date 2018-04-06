package wpd2.coursework1.model;

import wpd2.coursework1.util.H2DatabaseService;
import wpd2.coursework1.util.IoC;

import java.sql.Connection;

public abstract class BaseModel {
    protected static Connection getConnection() {
        H2DatabaseService databaseService = (H2DatabaseService) IoC.get().getInstance(H2DatabaseService.class);
        return databaseService.connect();
    }
}
