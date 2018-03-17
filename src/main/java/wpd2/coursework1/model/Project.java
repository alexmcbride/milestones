package wpd2.coursework1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project extends BaseModel {
    private int id;
    private String name;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    private Project() {
        // Private
    }

    @Override
    protected void validate() {
        // This is called by isValid() in the parent class to check if the model is valid.
        // If there are any validation errors after this method has been run the model will
        // be considered to be invalid.
        if (name == null || name.trim().length() == 0) {
            addValidationError("name", "Name is a required field");
        }

        if (created == null || created.getTime() == 0) {
            addValidationError("created", "Created is a required field");
        }
    }

    private static Project getProjectFromResult(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt(1));
        project.setName(resultSet.getString(2));
        project.setCreated(resultSet.getTimestamp(3));
        return project;
    }

    public void create() {
        String sql = "INSERT INTO projects (name, created) VALUES (?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, getName());
                statement.setTimestamp(2, new Timestamp(getCreated().getTime()));
                statement.executeUpdate();

                // Get ID
                ResultSet result = statement.getGeneratedKeys();
                if (result.next()) {
                    int id = result.getInt(1);
                    setId(id); // Set for model.
                }
            }
        }
        catch (Exception e) {
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Project create(String name) {
        Project project = empty();
        project.setName(name);
        project.setCreated(new Date());
        return project;
    }

    public static Project empty() {
        return new Project();
    }

    public static List<Project> loadAll() {
        String sql = "SELECT * FROM projects ORDER BY created DESC";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            // Query for projects.
            ResultSet result = statement.executeQuery(sql);

            // Create project list.
            List<Project> projects = new ArrayList<>();
            while (result.next()) {
                projects.add(getProjectFromResult(result));
            }
            return projects;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Project find(int id) {
        String sql = "SELECT * FROM projects WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            // Query for specific project.
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getProjectFromResult(result);
            }
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
