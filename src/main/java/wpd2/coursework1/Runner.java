
package wpd2.coursework1;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.servlet.ProjectListServlet;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    private static final int PORT = 9000;

    private void start() throws Exception {
        Server server = new Server(PORT);

        ServletContextHandler handler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        handler.setInitParameter("org.eclipse.jetty.servlet.Default." + "resourceBase", "src/main/resources/webapp");

        handler.addServlet(new ServletHolder(new ProjectListServlet()), "/projects");

        DefaultServlet ds = new DefaultServlet();
        handler.addServlet(new ServletHolder(ds), "/");

        server.start();
        LOG.info("Server started, will run until terminated");
        server.join();
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
