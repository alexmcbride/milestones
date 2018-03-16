package wpd2.coursework1.model;

import wpd2.coursework1.util.IoC;
import wpd2.coursework1.service.ConnectionService;
import wpd2.coursework1.service.H2ConnectionService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProjectTests {
    /*
     * Setup tests - create in-memory database for testing and seed it with some data.
     */
    @org.junit.Before
    public void setup() {
        // Init test database
        ConnectionService connectionService = new H2ConnectionService(ConnectionService.Mode.TEST);
        connectionService.initialize();
        connectionService.seed();

        // Register service for use in tests.
        IoC container = IoC.get();
        container.registerInstance(ConnectionService.class, connectionService);
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
        assertEquals("Project Name", project.getName());
        assertEquals(project.getId(), 1);
        assertNotNull(project.getCreated());
    }

    @org.junit.Test
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


}
