package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.util.IoC;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectTests {
    private DatabaseService databaseService;

    /*
     * Setup tests - create in-memory database for testing and seed it with some data.
     */
    @Before
    public void setup() {
        // Init test database
        databaseService = new H2DatabaseService(DatabaseService.Mode.TEST);
        databaseService.initialize();
        databaseService.seed();

        // Register service for use in test.
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, databaseService);
    }

    @After
    public void teardown() {
        // After each test destroy the database
        databaseService.destroy();
    }

    @Test
    public void testValidate() {
        Project project = new Project();
        project.setName("Test Title");
        assertTrue(project.isValid());
    }

    @Test
    public void testInvalidate() {
        Project project = new Project();
        assertFalse(project.isValid());
        assertEquals("Name is required", project.getValidationError("name"));
    }

    @Test
    public void testLoadAll() {
        List<Project> projects = Project.loadAll();
        assertEquals(projects.size(), 10);
    }

    @Test
    public void testFind() {
        Project project = Project.find(1);

        assertNotNull(project);
        assertEquals("Project Name 1", project.getName());
        assertEquals(project.getId(), 1);
        assertNotNull(project.getCreated());
    }

    @Test
    public void testCreate() {
        Date created = new Date();
        Project project = new Project();
        project.setName("Test");
        project.setCreated(created);

        project.create();
        int id = project.getId();

        Project result = Project.find(id);
        assertEquals("Test", result.getName());
        assertEquals(created, result.getCreated());
        assertEquals(id, result.getId());
    }

    @Test
    public void testUpdate() {
        Project project = new Project();
        project.setName("Test");
        project.setCreated(new Date());
        project.create();

        project.setName("Test 2");
        project.update();

        Project result = Project.find(project.getId());
        assertEquals("Test 2", result.getName());
    }

    @Test
    public void testDelete() {
        Project project = new Project();
        project.setName("Test");
        project.setCreated(new Date());
        project.create();

        project.delete();

        Project result = Project.find(project.getId());
        assertNull(result);
    }
}
