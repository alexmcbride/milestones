package wpd2.coursework1.model;

import wpd2.coursework1.util.ValidationError;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project extends BaseModel {
    private int id;
    private int userId;
    private String name;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        addValidationError("name", ValidationError.required(getName()));
    }

    public void create(User user) {
        setUserId(user.getId());
        try (Connection conn = getConnection()) {
            ProjectRepository repo = new ProjectRepository(conn);
            repo.insert(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        try (Connection conn = getConnection()) {
            ProjectRepository repo = new ProjectRepository(conn);
            repo.update(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        try (Connection conn = getConnection()) {
            ProjectRepository repo = new ProjectRepository(conn);
            repo.delete(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Project> loadAll() {
        try (Connection conn = getConnection()) {
            ProjectRepository repo = new ProjectRepository(conn);
            return repo.selectAll();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Project find(int id) {
        try (Connection conn = getConnection()) {
            ProjectRepository repo = new ProjectRepository(conn);
            return repo.select(id);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
