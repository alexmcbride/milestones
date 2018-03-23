package wpd2.coursework1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SharedProject extends BaseModel {
    private int projectId;
    private int userId;
    private Date shared;

    public int getProjectId() {
        return projectId;
    }

    private void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getShared() {
        return shared;
    }

    private void setShared(Date shared) {
        this.shared = shared;
    }

    public void create(Project project, User user) {
        shared = new Date();
        projectId = project.getId();
        userId = user.getId();

        String sql = "INSERT INTO sharedProjects (projectId, userId, shared) VALUES (?, ?, ?);";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sta.setInt(1, projectId);
            sta.setInt(2, userId);
            sta.setTimestamp(3, new Timestamp(shared.getTime()));
            sta.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        String sql = "DELETE FROM sharedProjects WHERE projectId=? AND userId=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, projectId);
            sta.setInt(2, userId);
            sta.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static SharedProject getSharedProjectFromResult(ResultSet result) throws SQLException {
        SharedProject sharedProject = new SharedProject();
        sharedProject.projectId = result.getInt(1);
        sharedProject.userId = result.getInt(2);
        sharedProject.shared = result.getTimestamp(3);
        return sharedProject;
    }

    public static SharedProject find(User user, Project project) {
        return find(user.getId(), project.getId());
    }

    public static SharedProject find(int userId, int projectId) {
        String sql = "SELECT projectId, userId, shared FROM sharedProjects WHERE projectId=? AND userId=?";
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, projectId);
            sta.setInt(2, userId);
            ResultSet result = sta.executeQuery();
            if (result.next()) {
                return getSharedProjectFromResult(result);
            }
            return null;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<SharedProject> findAll(User user) {
        return findForUser(user.getId());
    }

    public static List<SharedProject> findForUser(int userId) {
        String sql = "SELECT projectId, userId, shared FROM sharedProjects WHERE userId=?";
        return getSharedProjects(userId, sql);
    }

    public static List<SharedProject> findAll(Project project) {
        return findForUser(project.getId());
    }

    public static List<SharedProject> findForProject(int projectId) {
        String sql = "SELECT projectId, userId, shared FROM sharedProjects WHERE projectId=?";
        return getSharedProjects(projectId, sql);
    }

    private static List<SharedProject> getSharedProjects(int userId, String sql) {
        try (Connection conn = getConnection(); PreparedStatement sta = conn.prepareStatement(sql)) {
            sta.setInt(1, userId);
            ResultSet result = sta.executeQuery();
            List<SharedProject> sharedProjects = new ArrayList<>();
            while (result.next()) {
                sharedProjects.add(getSharedProjectFromResult(result));
            }
            return sharedProjects;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("CREATE TABLE IF NOT EXISTS sharedProjects ( " +
                    "projectId INTEGER NOT NULL," +
                    "userId INTEGER NOT NULL," +
                    "shared TIMESTAMP NOT NULL" +
                    ")");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        try (Connection conn = getConnection(); Statement sta = conn.createStatement()) {
            sta.execute("DROP TABLE IF EXISTS sharedProjects");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
