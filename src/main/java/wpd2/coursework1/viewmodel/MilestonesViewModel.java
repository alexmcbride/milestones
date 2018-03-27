package wpd2.coursework1.viewmodel;


import org.ocpsoft.prettytime.PrettyTime;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;
import java.util.List;
import java.util.ArrayList;


public class MilestonesViewModel {

    //  private Project project;
    private List<Milestone> milestones;
    private Project project;

    public MilestonesViewModel(Project project) {
        this.project = project;
        milestones = new ArrayList<>();
    }

    public List<Milestone> getMilestones() {

        return milestones;

    }

    public MilestonesViewModel(Project project, List<Milestone> milestones) {
        this.project = project;
        this.milestones = milestones;
    }


    public int getProjectId() {
        return project.getId();
    }

    public String getProjectName() {
        return project.getName();
    }


    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
    }


}
