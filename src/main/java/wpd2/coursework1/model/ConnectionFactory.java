package wpd2.coursework1.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
    private final ConnectionMode mode;

    public ConnectionFactory() {
        this(ConnectionMode.DEVELOPMENT);
    }

    public ConnectionFactory(ConnectionMode mode) {
        this.mode = mode;
    }

    public Connection build() throws SQLException, ClassNotFoundException {
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

    public void initialize() throws SQLException, ClassNotFoundException {
        Connection connection = build();
        createProjectsTable(connection);
    }

    private void createProjectsTable(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS projects;");
        statement.execute("CREATE TABLE projects (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100) NOT NULL , created TIMESTAMP NOT NULL);");
    }
}
