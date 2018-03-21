package wpd2.coursework1.model;

import wpd2.coursework1.util.ValidationHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Milestone extends ValidatableModel {
    private int id;
    private int projectId;
    private String name;
    private Date due;
    private Date actual;

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    private void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDue() {
        return due;
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public Date getActual() {
        return actual;
    }

    public void setActual(Date actual) {
        this.actual = actual;
    }

    @Override
    protected void validate() {
        ValidationHelper helper = new ValidationHelper(this);
        helper.required("name", name);
        helper.required("due", due);
    }

    public void create(Project project) {
        projectId = project.getId();

        String sql = "INSERT INTO milestones (projectId, name, due, actual) VALUES (?, ?, ?, ?);";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sta.setInt(1, projectId);
            sta.setString(2, name);
            sta.setTimestamp(3, new Timestamp(due.getTime()));
            if (actual != null) {
                sta.setTimestamp(4, new Timestamp(actual.getTime()));
            }
            sta.executeUpdate();

            ResultSet result = sta.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        String sql = "UPDATE milestones SET name=?, due=?, actual=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setString(1, name);
            sta.setTimestamp(2, new Timestamp(due.getTime()));
            if (getActual() != null) {
                sta.setTimestamp(3, new Timestamp(actual.getTime()));
            }
            sta.setInt(4, id);
            sta.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        String sql = "DELETE FROM milestones WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, id);
            sta.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Milestone find(int id) {
        String sql = "SELECT id, projectId, name, due, actual FROM milestones WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, id);
            ResultSet result = sta.executeQuery();
            if (result.next()) {
                return getMilestoneFromResult(result);
            }
            return null;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Milestone> findAll() {
        String sql = "SELECT id, projectId, name, due, actual FROM milestones";
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            List<Milestone> milestones = new ArrayList<>();
            ResultSet result = sta.executeQuery(sql);
            while (result.next()) {
                milestones.add(getMilestoneFromResult(result));
            }
            return milestones;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Milestone getMilestoneFromResult(ResultSet result) throws SQLException {
        Milestone milestone = new Milestone();
        milestone.id = result.getInt(1);
        milestone.projectId = result.getInt(2);
        milestone.name = result.getString(3);
        milestone.due = result.getTimestamp(4);
        milestone.actual = result.getTimestamp(5);
        return milestone;
    }

    public static void createTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("CREATE TABLE IF NOT EXISTS milestones ( " +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "projectId INTEGER NOT NULL," +
                "name NVARCHAR(250) NOT NULL," +
                "due TIMESTAMP NOT NULL," +
                "actual TIMESTAMP" +
                ")");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("DROP TABLE milestones");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
