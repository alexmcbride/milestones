package wpd2.coursework1.service;

import java.sql.*;

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
    public void initialize() {
        try (Connection connection = connect()) {
            createProjectsTable(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createProjectsTable(Connection connection) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS projects (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "name VARCHAR(100) NOT NULL , " +
                "created TIMESTAMP NOT NULL" +
                ")";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    /*
     * Destroy the database and drop all its tables.
     */
    @Override
    public void destroy() {
        try (Connection conn = connect(); Statement statement = conn.createStatement()) {
            statement.execute("DROP TABLE projects;");
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
        try (Connection connection = connect()) {
            seedProjectsTable(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void seedProjectsTable(Connection connection) throws SQLException {
        String sql = "INSERT INTO projects (name, created) VALUES (?, NOW())";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 10; i++) {
                statement.setString(1, "Project Name " + (i + 1));
                statement.executeUpdate();
            }
        }
    }
}
