package wpd2.coursework1.model;

import wpd2.coursework1.helper.ValidationHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Class to represent a project, a collection of milestones.
 */
public class Project extends ValidatableModel {
    private int id;
    private int userId;
    private String name;
    private Date created;
    private String username;
    private Date viewed;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getViewed() {
        return viewed;
    }

    public void setViewed(Date viewed) {
        this.viewed = viewed;
    }

    @Override
    public void validate() {
        ValidationHelper validation = new ValidationHelper(this);
        validation.required("name", name);
        validation.length("name", name, 1, 32);
    }

    public void create(User user) {
        userId = user.getId();
        username = user.getUsername();
        String sql = "INSERT INTO projects (userId, name, created, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setTimestamp(3, new Timestamp(created.getTime()));
            statement.setString(4, user.getUsername());
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        String sql = "UPDATE projects SET name=?, username=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setInt(3, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        // Delete all this project's milestones.
        List<Milestone> milestones = Milestone.findAll(this);
        for (Milestone milestone : milestones) {
            milestone.delete();
        }

        // Delete all times this project was shared.
        List<SharedProject> sharedProjects = SharedProject.findAll(this);
        for (SharedProject sharedProject : sharedProjects) {
            sharedProject.delete();
        }

        String sql = "DELETE FROM projects WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
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
                "created TIMESTAMP NOT NULL," +
                "username NVARCHAR(32) NOT NULL" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        String sql = "DROP TABLE IF EXISTS projects;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Project> findAll(User user) {
        return findAll(user.getId());
    }

    @SuppressWarnings("Duplicates")
    public static List<Project> findAll(int userId) {
        String sql = "SELECT id, userId, name, created, username FROM projects WHERE userId=?";
        List<Project> users = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, userId);
            ResultSet result = statement.executeQuery();
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
        String sql = "SELECT id, userId, name, created, username FROM projects WHERE id=?";
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
        project.id = resultSet.getInt(1);
        project.userId = resultSet.getInt(2);
        project.name = resultSet.getString(3);
        project.created = resultSet.getTimestamp(4);
        project.username = resultSet.getString(5);
        return project;
    }

    public List<User> getSharedUsers() {
        List<User> users = new ArrayList<>();
        List<SharedProject> sharedProjects = SharedProject.findAll(this);
        for (SharedProject sharedProject : sharedProjects) {
            User user = User.find(sharedProject.getUserId());
            users.add(user);
        }
        return users;
    }

    public boolean isOwnedBy(User user) {
        return isOwnedBy(user.getId());
    }

    public boolean isOwnedBy(int userId) {
        return this.userId == userId;
    }

    public boolean isReadOnly(User user) {
        return isReadOnly(user.getId());
    }

    public boolean isReadOnly(int userId) {
        SharedProject sharedProject = SharedProject.find(userId, id);
        if (sharedProject != null) {
            // Set viewed date if seen for first time.
            if (sharedProject.getViewed() == null) {
                sharedProject.setViewed(new Date());
                sharedProject.update();
            }
            return true;
        }
        return false;
    }
}
