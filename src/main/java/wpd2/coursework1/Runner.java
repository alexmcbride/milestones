package wpd2.coursework1;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Runner {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Runner.class);

    public static final int PORT = 9000;

    private void start() throws Exception {
        Server server = new Server(PORT);

        // Handler for web apps
        HandlerCollection handlers = new HandlerCollection();

        // Create web application context
        WebAppContext webapp = new WebAppContext();
        webapp.setResourceBase("src/main/resources/webapp"); // Where our WEB-INF lives.
        webapp.setContextPath("/");
        handlers.addHandler(webapp);

        // Adding the handler to the server
        server.setHandler(handlers);

        // Starting the Server
        server.start();
        LOG.info("Started: http://localhost:" + PORT + "/projects");
        server.join();
    }

    public static void main(String[] args) throws Exception {
        new Runner().start();
    }
}


