package wpd2.coursework1.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wpd2.coursework1.model.Project;
import wpd2.coursework1.model.SharedProject;
import wpd2.coursework1.model.User;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;

import java.util.List;

import static org.junit.Assert.*;

public class SharedProjectTests {
    private H2DatabaseService db;

    @Before
    public void setup() {
        db = new H2DatabaseService(DatabaseService.Mode.TEST);
        PasswordService pass = new PasswordService(PasswordService.MIN_COST);

        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, db);
        container.registerInstance(PasswordService.class, pass);

        db.initialize();
        db.seed();
    }

    @After
    public void teardown() {
        db.destroy();
    }

    @Test
    public void testFind() {
        User user = User.find(1);
        Project project = Project.find(1);

        SharedProject result = SharedProject.find(user, project);
        assertNotNull(result);
        assertEquals(1, result.getUserId());
        assertEquals(1, result.getProjectId());
        assertNotNull(result.getShared());
    }

    @Test
    public void testCreate() {
        Project project = Project.find(1);
        User user = User.find(1);

        SharedProject sharedProject = new SharedProject();
        sharedProject.create(project, user);

        SharedProject result = SharedProject.find(user, project);
        assertNotNull(result);
        assertNotNull(result.getShared());
    }

    @Test
    public void testDelete() {
        SharedProject sharedProject = SharedProject.find(1, 1);
        sharedProject.delete();
        sharedProject = SharedProject.find(1, 1);
        assertNull(sharedProject);
    }

    @Test
    public void testFindAll() {
        User user = User.find(1);
        Project project = Project.find(1);

        List<SharedProject> sharedProjects = SharedProject.findAll(user);
        assertEquals(10, sharedProjects.size());

        sharedProjects = SharedProject.findAll(project);
        assertEquals(10, sharedProjects.size());


    }
}
