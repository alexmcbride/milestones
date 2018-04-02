package wpd2.coursework1.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VelocityRenderer {
    private static final String TEMPLATE_DIR = "/templates/";
    private final VelocityContext context;

    public VelocityRenderer() {
        this.context = new VelocityContext();
    }

    public void addContext(String name, Object object) {
        context.put(name, object);
    }

    public void render(HttpServletResponse response, String templateName, Object object) throws IOException {
        addContext("model", object);
        Template template = Velocity.getTemplate(TEMPLATE_DIR + templateName);
        template.merge(context, response.getWriter());
    }

    public static void initializeTemplateEngine() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }
}
