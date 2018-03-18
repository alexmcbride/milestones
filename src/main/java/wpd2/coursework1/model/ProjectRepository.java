package wpd2.coursework1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    private final Connection conn;

    public ProjectRepository(Connection conn) {
        this.conn = conn;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS projects (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "userId INTEGER NOT NULL ," +
                "name NVARCHAR(32) NOT NULL , " +
                "created TIMESTAMP NOT NULL" +
                ")";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyTable() {
        String sql = "DROP TABLE projects;";
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void seedTable(List<Project> projects) {
        for (Project project : projects) {
            insert(project);
        }
    }

    public void insert(Project project) {
        String sql = "INSERT INTO projects (userId, name, created) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, project.getUserId());
            statement.setString(2, project.getName());
            statement.setTimestamp(3, new Timestamp(project.getCreated().getTime()));
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                project.setId(result.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Project project) {
        String sql = "UPDATE projects SET name=? WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, project.getName());
            statement.setInt(2, project.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Project project) {
        String sql = "DELETE FROM projects WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, project.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    public Project select(int id) {
        String sql = "SELECT id, userId, name, created FROM projects WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getUserFromResult(result);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public List<Project> selectAll() {
        String sql = "SELECT id, userId, name, created FROM projects";
        List<Project> users = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                users.add(getUserFromResult(result));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private static Project getUserFromResult(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt(1));
        project.setUserId(resultSet.getInt(2));
        project.setName(resultSet.getString(3));
        project.setCreated(resultSet.getTimestamp(4));
        return project;
    }
}
