package wpd2.coursework1.viewmodel;

import org.ocpsoft.prettytime.PrettyTime;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

public class ProjectViewModel {
    private Project project;
    private PrettyTime prettyTime;

    public ProjectViewModel(Project project) {
        this.project = project;
        this.prettyTime = new PrettyTime();
    }

    public int getId() {
        return project.getId();
    }

    public String getName() {
        return project.getName();
    }

    public String getCreated() {
        return prettyTime.format(project.getCreated());
    }

    public String getMilestones() {
        int count = Milestone.count(project);
        if (count > 0) {
            return String.valueOf(count);
        }
        return "None";
    }

    public String getNextMilestone() {
        Milestone milestone = Milestone.findNext(project);
        if (milestone != null) {
            return prettyTime.format(milestone.getDue());
        }
        return "Never";
    }
}
