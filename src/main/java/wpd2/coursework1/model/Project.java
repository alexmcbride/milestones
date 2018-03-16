package wpd2.coursework1.model;

import wpd2.coursework1.IoC;

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

    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        ConnectionFactory factory = (ConnectionFactory) IoC.get().getInstance(ConnectionFactory.class);
        return factory.build();
    }

    private static Project getProjectFromResult(ResultSet resultSet) throws SQLException {
        Project project = new Project();
        project.setId(resultSet.getInt(1));
        project.setName(resultSet.getString(2));
        project.setCreated(resultSet.getTimestamp(3));
        return project;
    }

    public void create() {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO projects (name, created) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Project> loadAll() {
        try (Connection conn = getConnection()) {
            // Query for projects.
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM projects ORDER BY created DESC");

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
        try (Connection conn = getConnection()) {
            // Query for specific project.
            String sql = "SELECT * FROM projects WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
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
