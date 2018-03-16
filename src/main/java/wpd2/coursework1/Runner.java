
package wpd2.coursework1;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.servlet.ProjectCreateServlet;
import wpd2.coursework1.servlet.ProjectDetailsServlet;
import wpd2.coursework1.servlet.ProjectIndexServlet;
import wpd2.coursework1.util.IoC;

import java.sql.SQLException;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9001;

    private void start() throws Exception {
        initializeServices();
        initializeTemplateEngine();
        initializeApp();
    }

    private void initializeApp() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        mapServletsToRoutes(handler);

        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        server.start();
        LOG.info("Server started, will run until terminated");
        LOG.info("Running: http://localhost:" + PORT + "/projects");
        server.join();
    }

    private void initializeServices() throws SQLException, ClassNotFoundException {
        // Init factory.
        DatabaseService databaseService = new H2DatabaseService();
        databaseService.initialize();

        // Init IoC stuff
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, databaseService);
    }

    private void initializeTemplateEngine() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }

    private void mapServletsToRoutes(ServletContextHandler handler) {
        handler.addServlet(new ServletHolder(new ProjectIndexServlet()), "/projects");
        handler.addServlet(new ServletHolder(new ProjectCreateServlet()), "/projects/create");
        handler.addServlet(new ServletHolder(new ProjectDetailsServlet()), "/projects/details");
    }

    public static void main(String[] args) {
        try {
            LOG.info("starting");
            new Runner().start();
        } catch (Exception e) {
            LOG.error("Unexpected error running shop: " + e.getMessage());
        }
    }
}
