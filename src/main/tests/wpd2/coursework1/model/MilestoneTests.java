package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;

import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

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
    public void testFind() {
        Milestone milestone = Milestone.find(1);
        assertNotNull(milestone);
    }
}