package wpd2.coursework1.model;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.util.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;

public class MilestoneTests {
    private DatabaseService db;

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, new PasswordServiceImpl(PasswordServiceImpl.MIN_COST));

        db.initialize();
        db.seed();
    }

    @After
    public void tearDown() {
        db.destroy();
    }

    @Test
    public void testCreateTable() {
        assertTrue(db.tableExists("milestones"));
    }

    @Test
    public void testValidate() {
        Milestone milestone = new Milestone();
        milestone.setName("Test name");
        milestone.setDue(new Date());
        milestone.setActual((Date)null);
        assertTrue(milestone.isValid());
    }

    @Test
    public void testInvalid() {
        Milestone milestone = new Milestone();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date actual = calendar.getTime();
        milestone.setActual(actual);
        assertFalse(milestone.isValid());

        assertEquals("Name must be between 1 and 250 characters.", milestone.getValidationError("name"));
        assertEquals("Due is required", milestone.getValidationError("due"));
        assertEquals("Actual must be in the past", milestone.getValidationError("actual"));
    }

    @Test
    public void testCreate() {
        Project project = Project.find(1);
        Date date = new Date();

        Milestone milestone = new Milestone();
        milestone.setName("Test");
        milestone.setDue(date);
        milestone.setActual(date);
        milestone.create(project);

        milestone = Milestone.find(milestone.getId());
        assertNotNull(milestone);
        assertNotNull(milestone.getProjectId());
        assertNotNull(milestone.getId());
        assertEquals("Test", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertNull(milestone.getActual());
        assertEquals(project.getMilestones(), 11);
    }

    @Test
    public void testUpdate() {
        Milestone milestone = Milestone.find(1);
        Date date = new Date();

        milestone.setName("Edited name");
        milestone.setDue(date);
        milestone.setActual(date);
        milestone.update();

        milestone = Milestone.find(milestone.getId());
        assertEquals("Edited name", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertEquals(date, milestone.getActual());
    }

    @Test
    public void testUpdateNoActual() {
        Milestone milestone = Milestone.find(1);
        Date date = new Date();

        milestone.setName("Edited name");
        milestone.setDue(date);
        milestone.setActual((Date)null);
        milestone.update();

        milestone = Milestone.find(milestone.getId());
        assertEquals("Edited name", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertNull(milestone.getActual());
    }

    @Test
    public void testDelete() {
        Milestone milestone = Milestone.find(1);
        milestone.delete();
        assertNull(Milestone.find(1));

        Project project = Project.find(milestone.getProjectId());
        assertEquals(9, project.getMilestones());
    }

    @Test
    public void testFind() {
        Milestone milestone = Milestone.find(1);
        assertNotNull(milestone);
    }

    @Test
    public void testFindAll() {
        Project project = Project.find(1);
        List<Milestone> milestones = Milestone.findAll(project);

        assertEquals(10, milestones.size());
    }

    @Test
    public void testCount() {
        Project project = Project.find(1);

        int count = Milestone.count(project);

        assertEquals(10, count);
    }

    @Test
    public void testFindNext() {
        Project project = Project.find(1);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);
        Date due = calendar.getTime();

        Milestone nowMilestone = new Milestone();
        nowMilestone.setName("test");
        nowMilestone.setDue(new Date());
        nowMilestone.setActual(new Date());
        nowMilestone.create(project);

        Milestone dueMilestone = new Milestone();
        dueMilestone.setName("test");
        dueMilestone.setDue(due);
        dueMilestone.setActual(new Date());
        dueMilestone.create(project);

        Milestone result = Milestone.findNext(project);
        assertEquals(due, result.getDue());
    }

    @Test
    public void testIsComplete() {
        Milestone milestone = new Milestone();
        assertFalse(milestone.isComplete());

        milestone.setActual(new Date());
        assertTrue(milestone.isComplete());
    }

    @Test
    public void testIsLate() {
        Milestone milestone = new Milestone();
        milestone.setDue(DateUtils.addDays(new Date(), -1));

        assertTrue(milestone.isLate());
    }

    @Test
    public void testIsCurrentWeek() {
        Milestone milestone = new Milestone();
        milestone.setDue(DateUtils.addDays(new Date(), 1));

        assertTrue(milestone.isCurrentWeek());
    }

    @Test
    public void testIsUpcoming() {
        Milestone milestone = new Milestone();
        milestone.setDue(DateUtils.addDays(new Date(), 8));

        assertTrue(milestone.isUpcoming());
    }
}