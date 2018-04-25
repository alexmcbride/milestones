package wpd2.coursework1.model;

import org.apache.commons.lang.time.DateUtils;
import wpd2.coursework1.helper.ValidationHelper;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * Class to represent a project milestone.
 */
public class Milestone extends ValidatableModel {
    private static final Date DATE = new Date();
    private static final Date DATE_PLUS_SEVEN = DateUtils.addDays(DATE, 7);

    private int id;
    private int projectId;
    private String name;
    private Date due;
    private Date actual;
    private boolean complete;

    public int getId() {
        return id;
    }

    private void setId(int id) {
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

    public boolean isComplete() {
        return complete;
    }

    public boolean isDone() {
        return complete || actual != null;
    }

    public boolean isLate() {
        return due.before(DATE) && !complete;
    }

    public boolean isCurrent() {
        return due.before(DATE_PLUS_SEVEN) && due.after(DATE) && !complete;
    }

    public boolean isUpcoming() {
        return due.after(DATE_PLUS_SEVEN) && !complete;
    }

    @Override
    protected void validate() {
        ValidationHelper helper = new ValidationHelper(this);
        helper.required("name", name);
        helper.length("name", name, 1, 250);
        helper.required("due", due);
    }

    @SuppressWarnings("Duplicates")
    public void create(Project project) {
        projectId = project.getId();

        String sql = "INSERT INTO milestones (projectId, name, due, complete) VALUES (?, ?, ?, ?);";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sta.setInt(1, projectId);
            sta.setString(2, name);
            sta.setTimestamp(3, new Timestamp(due.getTime()));
            sta.setBoolean(4, complete);
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
        String sql = "UPDATE milestones SET name=?, due=?, actual=?, complete=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setString(1, name);
            sta.setTimestamp(2, new Timestamp(due.getTime()));
            sta.setTimestamp(3, actual == null ? null : new Timestamp(actual.getTime()));
            sta.setBoolean(4, complete);
            sta.setInt(5, id);
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

    private static Milestone getMilestone(int projectId, String sql) {
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, projectId);
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

    public static Milestone find(int id) {
        String sql = "SELECT id, projectId, name, due, actual, complete FROM milestones WHERE id=?";
        return getMilestone(id, sql);
    }

    public static List<Milestone> findAll(Project project) {
        return findAll(project.getId());
    }

    public static List<Milestone> findAll(int projectId) {
        String sql = "SELECT id, projectId, name, due, actual, complete FROM milestones WHERE projectId=? ORDER BY due DESC";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, projectId);
            ResultSet result = sta.executeQuery();
            List<Milestone> milestones = new ArrayList<>();
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
        milestone.complete = result.getBoolean(6);
        return milestone;
    }

    public static int count(Project project) {
        return count(project.getId());
    }

    public static int count(int projectId) {
        String sql = "SELECT COUNT(*) FROM milestones WHERE projectId=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, projectId);
            ResultSet result = sta.executeQuery();
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Milestone findNext(Project project) {
        return findNext(project.getId());
    }

    public static Milestone findNext(int projectId) {
        String sql = "SELECT * FROM milestones WHERE due > NOW() AND projectId=? ORDER BY due LIMIT 1";
        return getMilestone(projectId, sql);
    }

    public static void createTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("CREATE TABLE IF NOT EXISTS milestones ( " +
                "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                "projectId INTEGER NOT NULL REFERENCES projects(id)," +
                "name NVARCHAR(250) NOT NULL," +
                "due TIMESTAMP NOT NULL," +
                "actual TIMESTAMP NULL," +
                "complete BOOLEAN NULL" +
                ")");

        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("DROP TABLE IF EXISTS milestones");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd H:m");

    private static Date parseDate(String value) throws ParseException {
        return format.parse(value);
    }

    private static String formatDate(Date value) {
        if (value != null) {
            return format.format(value);
        }
        return "";
    }

    public void setDue(String value) {
        try {
            due = parseDate(value);
        } catch (ParseException e) {
            addValidationError("due", "Due is not a valid date");
        }
    }

    public String getDueString() {
        return formatDate(getDue());
    }

    public void setActual(String value) {
        try {
            actual = parseDate(value);
        } catch (ParseException e) {
            addValidationError("actual", "Actual is not a valid date");
        }
    }

    public String getActualString() {
        return formatDate(getActual());
    }

    public void toggleComplete() {
        complete = !complete;
    }
}
