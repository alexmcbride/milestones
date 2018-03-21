package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.*;

public class MilestoneTests {

    private DatabaseService db;

    @Before
    public void setUp() {
        db = new H2DatabaseService();
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
        milestone.create(project);

        milestone = Milestone.find(milestone.getId());
        assertNotNull(milestone);
        assertNotNull(milestone.getProjectId());
        assertNotNull(milestone.getId());
        assertEquals("Test", milestone.getName());
        assertEquals(date, milestone.getDue());
        assertEquals(date, milestone.getActual());
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
        List<Milestone> milestones = Milestone.findAll();

        assertEquals(10, milestones.size());
    }
}