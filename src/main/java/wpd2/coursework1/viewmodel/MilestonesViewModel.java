package wpd2.coursework1.viewmodel;


import org.ocpsoft.prettytime.PrettyTime;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class MilestonesViewModel {

    //  private Project project;
    private List<Milestone> milestones;
    private Project project;
    private Date currentDate;
    private Date currentDatePlusSeven;

    public MilestonesViewModel(Project project, Date date) {
        this.project = project;
        milestones = new ArrayList<>();
        currentDate = date;

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

    public Date getCurrentDate() {
        return currentDate;
    }


    public Date getCurrentDatePlusSeven() {
        return currentDatePlusSeven;
    }
    public void setCurrentDatePlusSeven(Date datePlusSeven) {
        this.currentDatePlusSeven = datePlusSeven;
    }


    public String getProjectName() {
        return project.getName();
    }


    public void addMilestone(Milestone milestone) {
        milestones.add(milestone);
    }


}
