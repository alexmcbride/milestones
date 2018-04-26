package wpd2.coursework1.util;

import wpd2.coursework1.model.*;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/*
 * Class for creating H2 database connections.
 */
public class H2DatabaseService implements DatabaseService {
    private static final String DB_DATABASE_CONN = "jdbc:h2:~/test";
    private static final String DB_MEMORY_CONN = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private final Mode mode;

    /**
     * Creates a new H2DatabaseService in development mode.
     */
    public H2DatabaseService() {
        this(Mode.DEVELOPMENT);
    }

    /**
     * Creates a new H2DatabaseService in development mode.
     *
     * @param mode the mode to start the DB in.
     */
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

    /**
     * Checks if a table exists in the database.
     *
     * @param tableName the name of the true
     * @return true if it exists
     */
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
     * Seeds the database with data.
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
        User alex = new User();
        alex.setUsername("Alex");
        alex.setPassword("Password1".toCharArray());
        alex.setEmail("alex@email.com");
        alex.create();

        User user = new User();
        user.setUsername("Jeff");
        user.setPassword("Password1".toCharArray());
        user.setEmail("jeff@email.com");
        user.create();

        User steve = new User();
        steve.setUsername("Steve");
        steve.setPassword("Password1".toCharArray());
        steve.setEmail("steve@email.com");
        steve.create();

        User phil = new User();
        phil.setUsername("Phil");
        phil.setPassword("Password1".toCharArray());
        phil.setEmail("phil@email.com");
        phil.create();

        Project rspi = createProject("Research Skills", alex, 10);
        Project wpd = createProject("Web Platform Development 2", alex, 4);
        Project ip3 = createProject("Integrated Project 3", alex, 7);

        new SharedProject().create(rspi, steve);
        new SharedProject().create(rspi, phil);

        Project stevesProject = createProject("Steve's Project", steve, 12);
        new SharedProject().create(stevesProject, alex);
    }

    private Project createProject(String name, User owner, int count) {
        Project project = new Project();
        project.setName(name);
        project.create(owner);
        for (int i = 0; i < count; i++) {
            Milestone milestone = new Milestone();
            milestone.setName("Milestone " + (i + 1));
            milestone.setDue(getRandomDate());
            milestone.create(project);
        }
        return project;
    }

    private Date getRandomDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date start = calendar.getTime();
        calendar.add(Calendar.MONTH, 2);
        Date end = calendar.getTime();

        return new Date(ThreadLocalRandom.current().nextLong(start.getTime(), end.getTime()));
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
