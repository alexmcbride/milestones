package wpd2.coursework1.model;

import wpd2.coursework1.helper.ValidationHelper;

import java.sql.*;
import java.util.*;
import java.util.Date;

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
    private boolean open;
    private int milestones;

    /**
     * Gets the project ID.
     *
     * @return project ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the project ID.
     *
     * @param id project ID.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the user ID foreign key.
     *
     * @return user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user ID foreign key.
     *
     * @param userId user ID.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the project name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the project name.
     *
     * @param name the name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the project created date.
     *
     * @return the created date.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * Sets the project created date.
     *
     * @param created the created date.
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * Gets the username of the user who created this project.
     *
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user who created this project.
     *
     * @param username the username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets if this project has been viewed by the current user.
     *
     * @return the viewed date.
     */
    public Date getViewed() {
        return viewed;
    }

    /**
     * Sets if this project has been viewed by the current user.
     *
     * @param viewed the viewed date.
     */
    public void setViewed(Date viewed) {
        this.viewed = viewed;
    }

    /**
     * Gets if this project is open and public.
     *
     * @return true if public.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Sets if this project is open and public.
     *
     * @param open true if public.
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /**
     * Gets the number of milestones for this project.
     *
     * @return the milestones count.
     */
    public int getMilestones() {
        return milestones;
    }

    /**
     * Sets the number of milestones.
     *
     * @param milestones the count of milestones.
     */
    public void setMilestones(int milestones) {
        this.milestones = milestones;
    }

    @Override
    protected void validate() {
        ValidationHelper validation = new ValidationHelper(this);
        validation.required("name", name);
        validation.length("name", name, 1, 32);
    }

    /**
     * Inserts this project into the database.
     *
     * @param user the parent user for the project.
     */
    public void create(User user) {
        userId = user.getId();
        username = user.getUsername();
        created = new Date();

        String sql = "INSERT INTO projects (userId, name, created, username, open) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setString(2, name);
            statement.setTimestamp(3, new Timestamp(created.getTime()));
            statement.setString(4, username);
            statement.setBoolean(5, open);
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

    /**
     * Updates project in DB.
     */
    public void update() {
        String sql = "UPDATE projects SET name=?, username=?, open=?, milestones=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setBoolean(3, open);
            statement.setInt(4, milestones);
            statement.setInt(5, id);
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes project from DB, also deletes milestones and shared projects.
     */
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

    /**
     * Creates the projects table in the DB.
     */
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS projects (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "userId INTEGER NOT NULL REFERENCES users(id)," +
                "name NVARCHAR(32) NOT NULL , " +
                "created TIMESTAMP NOT NULL," +
                "username NVARCHAR(32) NOT NULL," +
                "open BOOLEAN NOT NULL DEFAULT false," +
                "milestones INTEGER NOT NULL DEFAULT 0" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Destroys the projects table from the DB.
     */
    public static void destroyTable() {
        String sql = "DROP TABLE IF EXISTS projects;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds all the projects that belong to the user.
     *
     * @param user the user to find projects for.
     * @return a list of projects.
     */
    public static List<Project> findAll(User user) {
        return findAll(user.getId());
    }

    /**
     * Finds all the projects that belong to the user.
     *
     * @param userId the ID of the user to find projects for.
     * @return a list of projects.
     */
    @SuppressWarnings("Duplicates")
    public static List<Project> findAll(int userId) {
        String sql = "SELECT * FROM projects WHERE userId=?";
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

    /**
     * Find a project with a specific ID.
     *
     * @param id the ID to find.
     * @return the project or null.
     */
    @SuppressWarnings("Duplicates")
    public static Project find(int id) {
        String sql = "SELECT * FROM projects WHERE id=?";
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
        project.open = resultSet.getBoolean(6);
        project.milestones = resultSet.getInt(7);
        return project;
    }

    /**
     * Get which users this project has been shared with.
     *
     * @return a list of users.
     */
    public List<User> getSharedUsers() {
        List<User> users = new ArrayList<>();
        List<SharedProject> sharedProjects = SharedProject.findAll(this);
        for (SharedProject sharedProject : sharedProjects) {
            User user = User.find(sharedProject.getUserId());
            users.add(user);
        }
        return users;
    }

    /**
     * Checks if this user owns the project (e.g. created it).
     *
     * @param user the user to check.
     * @return true if they own it.
     */
    public boolean isOwnedBy(User user) {
        if (user == null) {
            return false;
        }
        return isOwnedBy(user.getId());
    }

    /**
     * Checks if this user owns the project (e.g. created it).
     *
     * @param userId the ID of the user to check.
     * @return true if they own it.
     */
    public boolean isOwnedBy(int userId) {
        return this.userId == userId;
    }

    /**
     * Checks if the project has been shared with a specific user.
     *
     * @param user the user to check.
     * @return true if it has been shared.
     */
    public boolean hasBeenSharedWith(User user) {
        return hasBeenSharedWith(user.getId());
    }

    /**
     * Checks if the project has been shared with a specific user.
     *
     * @param userId the ID of the user to check.
     * @return true if it has been shared.
     */
    public boolean hasBeenSharedWith(int userId) {
        SharedProject sharedProject = SharedProject.find(userId, id);
        return sharedProject != null;
    }

    /**
     * Toggles the project open or closed for public viewing.
     */
    public void toggleOpen() {
        open = !open;
    }
}
