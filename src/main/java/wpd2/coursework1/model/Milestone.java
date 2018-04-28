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
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd H:m");

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
        return actual != null;
    }

    public boolean isLate() {
        return due.before(new Date()) && !isComplete();
    }

    public boolean isCurrentWeek() {
        Date now = new Date();
        return due.before(DateUtils.addDays(now, 7)) && due.after(now) && !isComplete();
    }

    public boolean isUpcoming() {
        return due.after(DateUtils.addDays(new Date(), 7))  && !isComplete();
    }

    @Override
    protected void validate() {
        ValidationHelper helper = new ValidationHelper(this);
        helper.required("name", name);
        helper.length("name", name, 1, 250);
        helper.required("due", due);
        helper.past("actual", actual);
    }

    @SuppressWarnings("Duplicates")
    public void create(Project project) {
        projectId = project.getId();

        String sql = "INSERT INTO milestones (projectId, name, due) VALUES (?, ?, ?);";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sta.setInt(1, projectId);
            sta.setString(2, name);
            sta.setTimestamp(3, new Timestamp(due.getTime()));
            sta.executeUpdate();

            ResultSet result = sta.getGeneratedKeys();
            if (result.next()) {
                id = result.getInt(1);
            }

            // Increment milestones count.
            project.setMilestones(project.getMilestones() + 1);
            project.update();
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
            sta.setTimestamp(3, actual == null ? null : new Timestamp(actual.getTime()));
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

            // Decrement project count.
            Project project = Project.find(projectId);
            project.setMilestones(project.getMilestones() - 1);
            project.update();
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
        String sql = "SELECT id, projectId, name, due, actual FROM milestones WHERE id=?";
        return getMilestone(id, sql);
    }

    public static List<Milestone> findAll(Project project) {
        return findAll(project.getId());
    }

    public static List<Milestone> findAll(int projectId) {
        String sql = "SELECT id, projectId, name, due, actual FROM milestones WHERE projectId=? ORDER BY due DESC";
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
                "actual TIMESTAMP NULL" +
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

    private static Date parseDate(String value) throws ParseException {
        return DATE_FORMAT.parse(value);
    }

    private static String formatDate(Date value) {
        if (value != null) {
            return DATE_FORMAT.format(value);
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
        if (value != null && value.trim().length() > 0) {
            try {
                actual = parseDate(value);
            } catch (ParseException e) {
                addValidationError("actual", "Actual is not a valid date");
            }
        }
        else {
            actual = null;
        }
    }

    public String getActualString() {
        return formatDate(getActual());
    }

    public void toggleComplete() {
        if (actual == null) {
            actual = new Date();
        }
        else {
            actual = null;
        }
    }
}
