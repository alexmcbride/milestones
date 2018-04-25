package wpd2.coursework1.util;

import wpd2.coursework1.model.*;

import java.sql.*;
import java.util.Date;

/*
 * Class for creating H2 database connections.
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
    public void initialize() {
        User.createTable();
        Project.createTable();
        Milestone.createTable();
        SharedProject.createTable();
        TempUser.createTable();
    }

    /*
     * Destroy the database and drop all its tables.
     */
    @Override
    public void destroy() {
        User.destroyTable();
        Project.destroyTable();
        Milestone.destroyTable();
        SharedProject.destroyTable();
        TempUser.destroyTable();
    }

    @Override
    public boolean tableExists(String tableName) {
        try (Connection conn = connect()) {
            ResultSet result = conn.getMetaData().getTables(null, null, tableName.toUpperCase(), null);
            return result.next();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Seeds the database with test data.
     */
    @Override
    public void seed() {
        if (mode == Mode.TEST) {
            seedTest();
        }
        else if (mode == Mode.DEVELOPMENT) {
            seedDevelopment();
        }
        else {
            throw new RuntimeException("Unknown DB connection mode");
        }
    }

    private void seedDevelopment() {
        User user = new User();
        user.setUsername("Alex");
        user.setPassword("Password1".toCharArray());
        user.setEmail("alex@email.com");
        user.create();

        user = new User();
        user.setUsername("Jeff");
        user.setPassword("Password1".toCharArray());
        user.setEmail("jeff@email.com");
        user.create();

        user = new User();
        user.setUsername("Steve");
        user.setPassword("Password1".toCharArray());
        user.setEmail("steve@email.com");
        user.create();

        user = new User();
        user.setUsername("Phil");
        user.setPassword("Password1".toCharArray());
        user.setEmail("phil@email.com");
        user.create();
    }

    private void seedTest() {
        User firstUser = null;
        Project firstProject = null;
        Milestone firstMilestone = null;

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

            if (firstProject == null) {
                firstProject = project;
            }
        }

        for (int i = 0; i < 10; i++) {
            Milestone milestone = new Milestone();
            milestone.setName("Milestone Name " + (i + 1));
            milestone.setActual(new Date());
            milestone.setDue(new Date());
            milestone.create(firstProject);

            if (firstMilestone == null) {
                firstMilestone = milestone;
            }
        }

        for (int i = 0; i < 10; i++) {
            SharedProject sharedProject = new SharedProject();
            sharedProject.create(firstProject, firstUser);
        }
    }
}
