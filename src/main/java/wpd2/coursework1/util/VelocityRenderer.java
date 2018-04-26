package wpd2.coursework1.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Renderer for rendering Velocity templates.
 */
public class VelocityRenderer {
    private static final String TEMPLATE_DIR = "/templates/";
    private final VelocityContext context;

    /**
     * Creates a new VelocityRenderer.
     */
    public VelocityRenderer() {
        this.context = new VelocityContext();
    }

    /**
     * Adds a context object to the view.
     *
     * @param name the name the object will be referenced by in the view.
     * @param object the object to add.
     */
    public void addContext(String name, Object object) {
        context.put(name, object);
    }

    /**
     * Renders a view to the response.
     *
     * @param response the response to render the view to.
     * @param templateName the name of the template to render.
     * @param object the object to include as the $model in views.
     * @throws IOException an error thrown.
     */
    public void render(HttpServletResponse response, String templateName, Object object) throws IOException {
        addContext("model", object);
        Template template = Velocity.getTemplate(TEMPLATE_DIR + templateName);
        template.merge(context, response.getWriter());
    }

    /**
     * Initializes the view system, this must be called before rendering a view.
     */
    public static void initialize() {
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();
    }
}
