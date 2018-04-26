package wpd2.coursework1.model;

import java.sql.*;

public class ProjectRepository extends BaseModel {
    public void create(Project project) {
        String sql = "INSERT INTO projects (userId, name, created, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, project.getUserId());
            statement.setString(2, project.getName());
            statement.setTimestamp(3, new Timestamp(project.getCreated().getTime()));
            statement.setString(4, project.getUsername());
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
}
