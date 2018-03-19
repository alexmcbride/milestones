package wpd2.coursework1.model;

import wpd2.coursework1.util.ValidationHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project extends BaseModel {
    private int id;
    private int userId;
    private String name;
    private Date created;

    public Project() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public void validate() {
        ValidationHelper validation = new ValidationHelper(this);
        validation.required("name", getName());
    }

    public void create(User user) {
        setUserId(user.getId());
        String sql = "INSERT INTO projects (userId, name, created) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, getUserId());
            statement.setString(2, getName());
            statement.setTimestamp(3, new Timestamp(getCreated().getTime()));
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                setId(result.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        String sql = "UPDATE projects SET name=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, getName());
            statement.setInt(2, getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        String sql = "DELETE FROM projects WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS projects (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "userId INTEGER NOT NULL ," +
                "name NVARCHAR(32) NOT NULL , " +
                "created TIMESTAMP NOT NULL" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        String sql = "DROP TABLE projects;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    public static List<Project> loadAll() {
        String sql = "SELECT id, userId, name, created FROM projects";
        List<Project> users = new ArrayList<>();
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
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

    @SuppressWarnings("Duplicates")
    public static Project find(int id) {
        String sql = "SELECT id, userId, name, created FROM projects WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
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

    private static Project getUserFromResult(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt(1));
        project.setUserId(resultSet.getInt(2));
        project.setName(resultSet.getString(3));
        project.setCreated(resultSet.getTimestamp(4));
        return project;
    }
}
