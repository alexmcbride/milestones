package wpd2.coursework1.model;

import wpd2.coursework1.util.IoC;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectTests {
    private DatabaseService databaseService;

    /*
     * Setup tests - create in-memory database for testing and seed it with some data.
     */
    @org.junit.Before
    public void setup() {
        // Init test database
        databaseService = new H2DatabaseService(DatabaseService.Mode.TEST);
        databaseService.initialize();
        databaseService.seed();

        // Register service for use in test.
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, databaseService);
    }

    @org.junit.After
    public void teardown() {
        // After each test destroy the database
        databaseService.destroy();
    }

    @org.junit.Test
    public void testLoadAll() {
        List<Project> projects = Project.loadAll();

        assertEquals(projects.size(), 10);
    }

    @org.junit.Test
    public void testFind() {
        Project project = Project.find(1);

        assertNotNull(project);
        assertEquals("Project Name 1", project.getName());
        assertEquals(project.getId(), 1);
        assertNotNull(project.getCreated());
    }

    @org.junit.Test
    public void testCreate() {
        Date created = new Date();
        Project project = Project.empty();
        project.setName("Test");
        project.setCreated(created);

        project.create();
        int id = project.getId();

        Project result = Project.find(id);
        assertEquals("Test", result.getName());
        assertEquals(created, result.getCreated());
        assertEquals(id, result.getId());
    }


}
