package wpd2.coursework1.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VelocityRenderer {
    private static final String TEMPLATE_DIR = "/templates/";
    private final AntiForgery antiForgery;

    public VelocityRenderer(AntiForgery antiForgery) {
        this.antiForgery = antiForgery;
    }

    public void render(HttpServletResponse response, String templateName, Object object) throws IOException {
        VelocityContext context = new VelocityContext();
        context.put("model", object);
        context.put("csrf", antiForgery);
        render(response, templateName, context);
    }

    public void render(HttpServletResponse response, String templateName, VelocityContext context) throws IOException {
        Template template = Velocity.getTemplate(TEMPLATE_DIR + templateName);
        template.merge(context, response.getWriter());
    }
}
