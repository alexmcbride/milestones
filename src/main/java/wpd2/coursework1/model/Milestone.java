package wpd2.coursework1.model;

import java.sql.*;
import java.util.Date;

public class Milestone extends BaseModel {
    private int id;
    private int projectId;
    private String name;
    private Date due;
    private Date actual;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
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

    }

    public void create(Project project) {
        setProjectId(project.getId());
        String sql = "INSERT INTO milestones (projectId, name, due, actual) VALUES (?, ?, ?, ?);";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sta.setInt(1, getProjectId());
            sta.setString(2, getName());
            sta.setTimestamp(3, new Timestamp(getDue().getTime()));
            if (getActual() != null) {
                sta.setTimestamp(4, new Timestamp(getActual().getTime()));
            }
            sta.executeUpdate();

            ResultSet result = sta.getGeneratedKeys();
            if (result.next()) {
                setId(result.getInt(1));
            }
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

    private static Milestone getMilestoneFromResult(ResultSet result) throws SQLException {
        Milestone milestone = new Milestone();
        milestone.setId(result.getInt(1));
        milestone.setProjectId(result.getInt(2));
        milestone.setName(result.getString(3));
        milestone.setDue(result.getTimestamp(4));
        milestone.setActual(result.getTimestamp(5));
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
