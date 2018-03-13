package wpd2.coursework1.model;

import javax.servlet.http.HttpSession;
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
                project.setCreated(new Date());
                projects.add(project);
            }
            session.setAttribute("projects", projects);
            return projects;
        }
        return (List<Project>)obj;
    }

    public void save(HttpSession session) {
        List<Project> projects = loadAll(session);
        this.setId(projects.size() + 1);
        projects.add(this);
        saveAll(session, projects);
    }

    public static Project find(HttpSession session, int id) {
        List<Project> projects = loadAll(session);
        for (Project project : projects) {
            if (project.getId() == id) {
                return project;
            }
        }
        return null;
    }
}
