package wpd2.coursework1.model;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class Project extends BaseModel {
    private int id;
    private String name;

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

    public static void saveAll(HttpSession session, List<Project> projects) {
        session.setAttribute("projects", projects);
    }

    public static List<Project> loadAll(HttpSession session) {
        Object obj = session.getAttribute("projects");
        if (obj == null) {
            List<Project> projects = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Project project = new Project();
                project.setId(i);
                project.setName("Name " + i);
                projects.add(project);
            }
            session.setAttribute("projects", projects);
            return projects;
        }
        return (List<Project>)obj;
    }

    public void save(HttpSession session) {
        List<Project> projects = loadAll(session);
        projects.add(this);
        saveAll(session, projects);
    }
}
