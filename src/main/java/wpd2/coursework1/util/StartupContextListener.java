package wpd2.coursework1.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.servlet.BaseServlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * Class to handle server startup event, init DB and register services.
 */
public class StartupContextListener implements ServletContextListener {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    private static final boolean RESET_DATABASE_ON_STARTUP = true;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Initializing services");
        initializeServices();
        initializeDatabase();
        VelocityRenderer.initialize();
    }

    private void initializeServices() {
        // Services that need to be injected during unit tests.
        IoC container = IoC.get();
        container.registerInstance(H2DatabaseService.class, new H2DatabaseService());
        container.registerInstance(PasswordService.class, new PasswordService());
    }

    private void initializeDatabase() {
        DatabaseService databaseService = (DatabaseService)IoC.get().getInstance(H2DatabaseService.class);

        if (RESET_DATABASE_ON_STARTUP) {
            databaseService.destroy();
            databaseService.initialize();
            databaseService.seed();
        }
        else {
            databaseService.initialize();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        /* Not used */
    }
}