package wpd2.coursework1.viewmodel;


import org.ocpsoft.prettytime.PrettyTime;
import wpd2.coursework1.model.Milestone;
import wpd2.coursework1.model.Project;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;


public class MilestonesViewModel {

    //  private Project project;
    private List<Milestone> lateMilestones;
    private List<Milestone> doneMilestones;
    private List<Milestone> currentMilestones;
    private List<Milestone> upcomingMilestones;

    private Project project;
    private Date currentDate;
    private Date currentDatePlusSeven;

    public MilestonesViewModel(Project project, Date date) {
        this.project = project;
        doneMilestones = new ArrayList<>();
        lateMilestones = new ArrayList<>();
        currentMilestones = new ArrayList<>();
        upcomingMilestones = new ArrayList<>();

        currentDate = date;

    }


    // getters for lists of milestones
    public List<Milestone> getLateMilestones() { return lateMilestones; }
    public List<Milestone> getDoneMilestones() { return doneMilestones; }
    public List<Milestone> getCurrentMilestones() { return currentMilestones; }
    public List<Milestone> getUpcomingMilestones() { return upcomingMilestones; }


    // setters for lists of milestones
    public void setLateMilestones(List<Milestone> getLateMilestones) { this.lateMilestones = getLateMilestones; }
    public void setDoneMilestones(List<Milestone> getLateMilestones) { this.doneMilestones = getLateMilestones; }
    public void setCurrentMilestones(List<Milestone> getLateMilestones) { this.currentMilestones = getLateMilestones; }
    public void setUpcomingMilestones(List<Milestone> getLateMilestones) { this.upcomingMilestones = getLateMilestones; }



//, List<Milestone> doneMilestones, List<Milestone> lateMilestones, List<Milestone> currentMilestones, List<Milestone> upcomingMilestones
    public MilestonesViewModel(Project project) {
        this.project = project;
        this.doneMilestones = new ArrayList<>();
        this.lateMilestones = new ArrayList<>();
        this.currentMilestones = new ArrayList<>();
        this.upcomingMilestones = new ArrayList<>();
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



    // Adding milestones to appropriate lists
    public void addLateMilestone(Milestone milestone) {
        lateMilestones.add(milestone);
    }
    public void addDoneMilestone(Milestone milestone) {
        doneMilestones.add(milestone);
    }
    public void addCurrentMilestone(Milestone milestone) {
        currentMilestones.add(milestone);
    }
    public void addUpcomingMilestone(Milestone milestone) {
        upcomingMilestones.add(milestone);
    }


}
