package wpd2.coursework1.viewmodel;

import wpd2.coursework1.model.Project;

import java.util.List;

public class ProjectListViewModel {
    private final List<Project> projects;

    public ProjectListViewModel(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
