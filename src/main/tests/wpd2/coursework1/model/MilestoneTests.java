package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.util.DatabaseService;
import wpd2.coursework1.util.PasswordService;
import wpd2.coursework1.util.IoC;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;

public class MilestoneTests {

    private DatabaseService db;

    @SuppressWarnings("Duplicates")
    @Before
    public void setUp() {
        db = new DatabaseService(DatabaseService.Mode.TEST);
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, new PasswordService(PasswordService.MIN_COST));
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
        assertTrue(milestone.isValid());
    }

    @Test
    public void testInvalid() {
        Milestone milestone = new Milestone();
        assertFalse(milestone.isValid());

        assertEquals("Name is required", milestone.getValidationError("name"));
        assertEquals("Due is required", milestone.getValidationError("due"));
    }

    @Test
    public void testCreate() {
        Project project = Project.find(1);
        Date date = new Date();

        Milestone milestone = new Milestone();
        milestone.setName("Test");
        milestone.setDue(date);
        milestone.setActual(date);
        milestone.setComplete(true);
        milestone.create(project);

        milestone = Milestone.find(milestone.getId());
        assertNotNull(milestone);
        assertNotNull(milestone.getProjectId());
        assertNotNull(milestone.getId());
        assertEquals("Test", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertNull(milestone.getActual());
        assertTrue(milestone.isComplete());
    }

    @Test
    public void testUpdate() {
        Milestone milestone = Milestone.find(1);
        Date date = new Date();

        milestone.setName("Edited name");
        milestone.setDue(date);
        milestone.setActual(date);
        milestone.setComplete(true);
        milestone.update();

        milestone = Milestone.find(milestone.getId());
        assertEquals("Edited name", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertEquals(date, milestone.getActual());
        assertTrue(milestone.isComplete());
    }

    @Test
    public void testUpdateNoActual() {
        Milestone milestone = Milestone.find(1);
        Date date = new Date();

        milestone.setName("Edited name");
        milestone.setDue(date);
        milestone.setActual((Date)null);
        milestone.setComplete(true);
        milestone.update();

        milestone = Milestone.find(milestone.getId());
        assertEquals("Edited name", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertNull(milestone.getActual());
        assertTrue(milestone.isComplete());
    }

    @Test
    public void testDelete() {
        Milestone milestone = Milestone.find(1);
        milestone.delete();
        assertNull(Milestone.find(1));
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
}