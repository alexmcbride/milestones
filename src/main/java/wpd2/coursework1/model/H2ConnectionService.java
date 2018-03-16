package wpd2.coursework1.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Factory class for creating H2 database connections.
 */
public class H2ConnectionService implements ConnectionService {
    private final Mode mode;

    public H2ConnectionService() {
        this(Mode.DEVELOPMENT);
    }

    public H2ConnectionService(Mode mode) {
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
                    return DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                case TEST:
                    // Use in-memory DB for testing.
                    return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
                default:
                    throw new RuntimeException("Unknown DB connection mode");
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize() {
        Connection connection = connect();
        try {
            createProjectsTable(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void createProjectsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("CREATE TABLE IF NOT EXISTS projects (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL , created TIMESTAMP NOT NULL);");
    }

    @Override
    public void seed() {
        Connection connection = connect();
        try {
            seedProjectsTable(connection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void seedProjectsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        for (int i = 0; i < 10; i++) {
            statement.execute("INSERT INTO projects (name, created) VALUES ('Test Project', NOW());");
        }
    }
}
