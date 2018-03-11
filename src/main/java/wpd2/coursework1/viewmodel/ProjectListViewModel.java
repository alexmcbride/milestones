package wpd2.coursework1.viewmodel;

import wpd2.coursework1.model.Project;

import java.util.List;

public class ProjectListViewModel {
    private Project project;
    private List<Project> projects;

    public ProjectListViewModel(Project project, List<Project> projects) {
        this.project = project;
        this.projects = projects;
    }

    public Project getProject() {
        return project;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
