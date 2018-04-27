package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.util.*;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectTests {
    private DatabaseService db;

    /*
     * Setup tests - create in-memory database for testing and seed it with some data.
     */
    @SuppressWarnings("Duplicates")
    @Before
    public void setup() {
        // Init test database
        db = new H2DatabaseService(DatabaseService.Mode.TEST);

        // Register service for use in test.
        IoC container = IoC.get();
        container.registerInstance(H2DatabaseService.class, db);
        container.registerInstance(PasswordService.class, new PasswordServiceImpl(PasswordServiceImpl.MIN_COST));

        db.initialize();
        db.seed();
    }

    @After
    public void teardown() {
        // After each test destroy the database
        db.destroy();
    }

    @Test
    public void testValidate() {
        Project project = new Project();
        project.setName("Test Title");
        assertTrue(project.isValid());
    }

    @Test
    public void testInvalid() {
        Project project = new Project();
        assertFalse(project.isValid());
        assertEquals("Name must be between 1 and 32 characters.", project.getValidationError("name"));
    }

    @Test
    public void testLoadAll() {
        User user = User.find(1);
        List<Project> projects = Project.findAll(user);
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
        User user = User.find(1);

        Date created = new Date();
        Project project = new Project();
        project.setName("Test");
        project.setCreated(created);

        project.create(user);
        int id = project.getId();

        Project result = Project.find(id);
        assertEquals("Test", result.getName());
        assertEquals(created, result.getCreated());
        assertEquals(id, result.getId());
    }

    @Test
    public void testUpdate() {
        User user = User.find(1);

        Project project = new Project();
        project.setName("Test");
        project.setCreated(new Date());
        project.create(user);

        project.setName("Test 2");
        project.update();

        Project result = Project.find(project.getId());
        assertEquals("Test 2", result.getName());
    }

    @Test
    public void testDelete() {
        User user = User.find(1);

        Project project = new Project();
        project.setName("Test");
        project.setCreated(new Date());
        project.create(user);

        project.delete();

        Project result = Project.find(project.getId());
        assertNull(result);
    }

    @Test
    public void testGetSharedUsers() {
        User user = User.find(1);

        Project project = new Project();
        project.setName("Test");
        project.setUsername("Tester");
        project.setCreated(new Date());
        project.create(user);

        User user2 = User.find(2);
        SharedProject sharedProject = new SharedProject();
        sharedProject.create(project, user2);

        User user3 = User.find(3);
        sharedProject = new SharedProject();
        sharedProject.create(project, user3);

        List<User> users = project.getSharedUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testIsOwnedBy() {
        User user = User.find(1);

        Project project = new Project();
        project.setUserId(user.getId());

        assertTrue(project.isOwnedBy(user));
        assertTrue(project.isOwnedBy(user.getId()));
    }

    @Test
    public void testIsReadOnly() {
        User user = User.find(1);

        Project project = new Project();
        project.setName("Test");
        project.setUsername("Tester");
        project.setCreated(new Date());
        project.create(user);

        User user2 = User.find(2);
        SharedProject sharedProject = new SharedProject();
        sharedProject.create(project, user2);

        assertTrue(project.hasBeenSharedWith(user2));
        assertTrue(project.hasBeenSharedWith(user2.getId()));

        sharedProject = SharedProject.find(user2, project);
        assertNotNull(sharedProject.getViewed());
    }
}
