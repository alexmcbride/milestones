package wpd2.coursework1.model;

import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModel {
    private int id;
    private String name;

    public static List<Project> loadAll() {
        List<Project> projects = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Project project = new Project();
            project.setId(i);
            project.setName("Name " + i);
            projects.add(project);
        }
        return projects;
    }

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

    @Override
    protected void validate() {
        if (name == null || name.trim().length() == 0) {
            addValidationError("name", "Name is a required field");
        }
    }
}
