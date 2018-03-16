
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
import wpd2.coursework1.model.ConnectionService;
import wpd2.coursework1.model.H2ConnectionService;
import wpd2.coursework1.servlet.ProjectCreateServlet;
import wpd2.coursework1.servlet.ProjectDetailsServlet;
import wpd2.coursework1.servlet.ProjectIndexServlet;

import java.sql.SQLException;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9001;

    private void start() throws Exception {
        initializeTemplateEngine();
        registerServices();

        // Init server
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        handler.addServlet(new ServletHolder(new ProjectIndexServlet()), "/projects");
        handler.addServlet(new ServletHolder(new ProjectCreateServlet()), "/projects/create");
        handler.addServlet(new ServletHolder(new ProjectDetailsServlet()), "/projects/details");

        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        server.start();
        LOG.info("Server started, will run until terminated");
        LOG.info("Browser: http://localhost:" + PORT + "/projects");
        server.join();
    }

    private void initializeTemplateEngine() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }

    private void registerServices() throws SQLException, ClassNotFoundException {
        // Init factory.
        ConnectionService connectionService = new H2ConnectionService();
        connectionService.initialize();

        // Init IoC stuff
        IoC container = IoC.get();
        container.registerInstance(ConnectionService.class, connectionService);
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
