package wpd2.coursework1.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * Factory class for creating H2 database connections.
 */
public class H2ConnectionFactory implements ConnectionFactory {
    private final Mode mode;

    public H2ConnectionFactory() {
        this(Mode.DEVELOPMENT);
    }

    public H2ConnectionFactory(Mode mode) {
        this.mode = mode;
    }

    public Connection build(){
        try {
            Class.forName("org.h2.Driver");
            switch (mode) {
                case DEVELOPMENT:
                    return DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
                case TEST:
                    return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
                default:
                    throw new RuntimeException("Unknown connection mode");
            }
        }
        catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        Connection connection = build();
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
}
