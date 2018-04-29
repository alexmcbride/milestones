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

    /**
     * Gets the ID of the milestone.
     *
     * @return the ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID.
     *
     * @param id the ID.
     */
    private void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the project ID foreign key.
     *
     * @return the project ID.
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Sets the foreign key, changing this milestones parent.
     *
     * @param projectId the ID to set.
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the name of the project.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the date this milestone is due.
     *
     * @return the due date.
     */
    public Date getDue() {
        return due;
    }

    /**
     * Sets the date this milestone is due.
     *
     * @param due the due date.
     */
    public void setDue(Date due) {
        this.due = due;
    }

    /**
     * Gets the actual completion date of the milestone.
     *
     * @return the actual date.
     */
    public Date getActual() {
        return actual;
    }

    /**
     * Sets the actual completion date of the milestone, setting the milestone into a 'completed' state.
     *
     * @param actual the date to set.
     */
    public void setActual(Date actual) {
        this.actual = actual;
    }

    /**
     * Gets if the milestone is complete.
     *
     * @return true if actual has been set.
     */
    public boolean isComplete() {
        return actual != null;
    }

    /**
     * Gets if the milestone is late.
     *
     * @return boolean
     */
    public boolean isLate() {
        return due.before(new Date()) && !isComplete();
    }

    /**
     * Gets if the milestone is due this week.
     *
     * @return boolean
     */
    public boolean isCurrentWeek() {
        Date now = new Date();
        return due.before(DateUtils.addDays(now, 7)) && due.after(now) && !isComplete();
    }

    /**
     * Gets if the milestone is due after this week.
     *
     * @return boolean
     */
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

    /**
     * Inserts the milestone into the DB.
     *
     * @param project the parent project for the milestone.
     */
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

    /**
     * Updates the current state of the milestone to DB.
     */
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

    /**
     * Deletes this milestone from the DB.
     */
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

    /**
     * Find a particular milestone by ID.
     *
     * @param id the ID of the milestone to find.
     * @return a milestone object or null if not found.
     */
    public static Milestone find(int id) {
        String sql = "SELECT id, projectId, name, due, actual FROM milestones WHERE id=?";
        return getMilestone(id, sql);
    }

    /**
     * Finds all the milestones for the project.
     *
     * @param project the project to find milestones for.
     * @return a list of milestones.
     */
    public static List<Milestone> findAll(Project project) {
        return findAll(project.getId());
    }

    /**
     * Finds all the milestones for the project.
     *
     * @param projectId the ID project to find milestones for.
     * @return a list of milestones.
     */
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

    /**
     * Counts number of milestones a project has.
     *
     * @param project the project to count for.
     * @return the number of milestones.
     */
    public static int count(Project project) {
        return count(project.getId());
    }

    /**
     * Counts number of milestones a project has.
     *
     * @param projectId the ID of the project to count for.
     * @return the number of milestones.
     */
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

    /**
     * Finds the next milestone in a project from the current date.
     *
     * @param project the project to find the milestone for.
     * @return the milestone or null if none exists.
     */
    public static Milestone findNext(Project project) {
        return findNext(project.getId());
    }

    /**
     * Finds the next milestone in a project from the current date.
     *
     * @param projectId the ID of the project to find the milestone for.
     * @return the milestone or null if none exists.
     */
    public static Milestone findNext(int projectId) {
        String sql = "SELECT * FROM milestones WHERE due > NOW() AND projectId=? ORDER BY due LIMIT 1";
        return getMilestone(projectId, sql);
    }

    /**
     * Creates the milestones table in the DB.
     */
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

    /**
     * Destroys the milestones table.
     */
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

    /**
     * Sets the current due date as a string, so it can be validated.
     *
     * @param value the date as a parseable string.
     */
    public void setDue(String value) {
        try {
            due = parseDate(value);
        } catch (ParseException e) {
            addValidationError("due", "Due is not a valid date");
        }
    }

    /**
     * Gets the current due time as a formatted string, for output in a form element.
     *
     * @return the date string.
     */
    public String getDueString() {
        return formatDate(getDue());
    }

    /**
     * Sets the current actual completion date as a string, so it can be validated.
     *
     * @param value the date as a parseable string.
     */
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

    /**
     * Gets the current actual time as a formatted string, for output in a form element.
     *
     * @return the date string.
     */
    public String getActualString() {
        return formatDate(getActual());
    }

    /**
     * Toggles whether the milestone is complete or not, by setting actual to the current date.
     */
    public void toggleComplete() {
        if (actual == null) {
            actual = new Date();
        }
        else {
            actual = null;
        }
    }
}
