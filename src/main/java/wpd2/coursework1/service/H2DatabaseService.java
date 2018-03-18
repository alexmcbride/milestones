package wpd2.coursework1.service;

import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.User;

import java.sql.*;
import java.util.Date;

/*
 * Factory class for creating H2 database connections.
 */
public class H2DatabaseService implements DatabaseService {
    private static final String DB_DATABASE_CONN = "jdbc:h2:~/test";
    private static final String DB_MEMORY_CONN = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private final Mode mode;

    public H2DatabaseService() {
        this(Mode.DEVELOPMENT);
    }

    public H2DatabaseService(Mode mode) {
        this.mode = mode;
    }

    /*
     * Create new connection for required mode.
     */
    @Override
    public Connection connect(){
        try {
            Class.forName("org.h2.Driver");
            switch (mode) {
                case DEVELOPMENT:
                    return DriverManager.getConnection(DB_DATABASE_CONN, DB_USER, DB_PASSWORD);
                case TEST:
                    // Use in-memory DB for testing.
                    return DriverManager.getConnection(DB_MEMORY_CONN);
                default:
                    throw new RuntimeException("Unknown DB connection mode");
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Creates the database tables.
     */
    @Override
    public void initialize() throws SQLException {
        try (Connection conn = connect()) {
            User.createTable();
            Project.createTable();
        }
    }

    /*
     * Destroy the database and drop all its tables.
     */
    @Override
    public void destroy() throws SQLException {
        try (Connection conn = connect()) {
            User.destroyTable();
            Project.destroyTable();
        }
    }

    /*
     * Seeds the database with test data.
     */
    @Override
    public void seed() throws SQLException {
        User firstUser = null;

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setUsername("Username" + i);
            user.setEmail("user" + i + "@email.com");
            user.setPassword("password1".toCharArray());
            user.create();

            if (firstUser == null) {
                firstUser = user;
            }
        }

        for (int i = 0; i < 10; i++) {
            Project project = new Project();
            project.setUserId(1);
            project.setCreated(new Date());
            project.setName("Project Name " + (i + 1));
            project.create(firstUser);
        }
    }
}
