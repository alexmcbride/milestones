package wpd2.coursework1.tests;

import wpd2.coursework1.IoC;
import wpd2.coursework1.model.ConnectionService;
import wpd2.coursework1.model.H2ConnectionService;
import wpd2.coursework1.model.Project;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void testProjectList() {
        List<Project> projects = Project.loadAll();

        assertEquals(projects.size(), 10);
    }
}
