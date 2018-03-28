package wpd2.coursework1;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.service.DatabaseService;
import wpd2.coursework1.service.H2DatabaseService;
import wpd2.coursework1.servlet.*;
import wpd2.coursework1.servlet.MilestoneIndexServlet;
import wpd2.coursework1.service.PasswordService;
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
        VelocityRenderer.initializeTemplateEngine();

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

    private void initializeServices() {
        // Services that need to be injected during unit tests.
        IoC container = IoC.get();
        container.registerInstance(DatabaseService.class, new H2DatabaseService());
        container.registerInstance(PasswordService.class, new PasswordService());
    }

    private void initializeDatabase() {
        DatabaseService databaseService = (DatabaseService)IoC.get().getInstance(DatabaseService.class);
        if (RESET_DATABASE_ON_STARTUP) {
            databaseService.destroy();
        }
        databaseService.initialize();
    }

    private void mapServletsToRoutes(ServletContextHandler handler) {
        handler.addServlet(new ServletHolder(new ProjectIndexServlet()), "/projects");
        handler.addServlet(new ServletHolder(new ProjectCreateServlet()), "/projects/create");
        handler.addServlet(new ServletHolder(new ProjectDetailsServlet()), "/projects/details");

        handler.addServlet(new ServletHolder(new UserRegisterServlet()), "/users/register");
        handler.addServlet(new ServletHolder(new UserLoginServlet()), "/users/login");
        handler.addServlet(new ServletHolder(new UserAccountServlet()), "/users/account");
        handler.addServlet(new ServletHolder(new UserPwResetEmailServlet()), "/users/pw_reset_email");
        handler.addServlet(new ServletHolder(new UserPwResetEmailSentServlet()), "/users/pw_reset_email_sent");
        handler.addServlet(new ServletHolder(new UserPwResetServlet()), "/users/pw_reset");
        handler.addServlet(new ServletHolder(new UserDeleteServlet()), "/users/delete");
        handler.addServlet(new ServletHolder(new UserLogoutServlet()), "/users/logout");

        handler.addServlet(new ServletHolder(new MilestoneIndexServlet()), "/milestone");
    }

    public static void main(String[] args) throws Exception {
        new Runner().start();
    }
}

