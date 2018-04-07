package wpd2.coursework1;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.util.H2DatabaseService;

import wpd2.coursework1.util.PasswordService;
import wpd2.coursework1.util.IoC;
import wpd2.coursework1.util.VelocityRenderer;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9000;
    private static final boolean RESET_DATABASE_ON_STARTUP = false;

    private void start() throws Exception {
        initializeServices();
        initializeDatabase();
        VelocityRenderer.initialize();

        Server server = new Server(PORT);

        // Handler for web apps
        HandlerCollection handlers = new HandlerCollection();

        // Create web application context
        WebAppContext webapp = new WebAppContext();
        webapp.setResourceBase("src/main/resources/webapp"); // Where our WEB-INF lives.
        webapp.setContextPath("/");
//        webapp.addServlet(new ServletHolder(new DefaultServlet()), "/");
        handlers.addHandler(webapp);

        // Adding the handler to the server
        server.setHandler(handlers);

        // Starting the Server
        server.start();
        LOG.info("Started: http://localhost:" + PORT + "/projects");
        server.join();
    }

    private void initializeServices() {
        // Services that need to be injected during unit tests.
        IoC container = IoC.get();
        container.registerInstance(H2DatabaseService.class, new H2DatabaseService());
        container.registerInstance(PasswordService.class, new PasswordService());
    }

    private void initializeDatabase() {
        H2DatabaseService databaseService = (H2DatabaseService)IoC.get().getInstance(H2DatabaseService.class);
        if (RESET_DATABASE_ON_STARTUP) {
            databaseService.destroy();
            databaseService.initialize();
            databaseService.seed();
        }
        else {
            databaseService.initialize();
        }
    }

    public static void main(String[] args) throws Exception {
        new Runner().start();
    }
}


