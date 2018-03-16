package wpd2.coursework1.tests;

import wpd2.coursework1.IoC;
import wpd2.coursework1.model.ConnectionFactory;
import wpd2.coursework1.model.H2ConnectionFactory;
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
        ConnectionFactory factory = new H2ConnectionFactory(ConnectionFactory.Mode.TEST);
        factory.initialize();
        factory.seed();

        // Register service for use in tests.
        IoC container = IoC.get();
        container.registerInstance(ConnectionFactory.class, factory);
    }

    @org.junit.Test
    public void testProjectList() {
        List<Project> projects = Project.loadAll();

        assertEquals(projects.size(), 10);
    }
}
