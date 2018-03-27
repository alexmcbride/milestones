package wpd2.coursework1.util;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class VelocityRenderer {
    private static final String TEMPLATE_DIR = "/templates/";
    private final AntiForgeryHelper antiForgeryHelper;
    private final UserManager userManager;
    private final FlashHelper flashHelper;

    public VelocityRenderer(AntiForgeryHelper antiForgeryHelper, UserManager userManager, FlashHelper flashHelper) {
        this.antiForgeryHelper = antiForgeryHelper;
        this.userManager = userManager;
        this.flashHelper = flashHelper;
    }

    public void render(HttpServletResponse response, String templateName, Object object) throws IOException {
        VelocityContext context = new VelocityContext();
        context.put("model", object);
        context.put("antiForgeryHelper", antiForgeryHelper);
        context.put("userManager", userManager);
        context.put("flashHelper", flashHelper);
        render(response, templateName, context);
    }

    public void render(HttpServletResponse response, String templateName, VelocityContext context) throws IOException {
        Template template = Velocity.getTemplate(TEMPLATE_DIR + templateName);
        template.merge(context, response.getWriter());
    }
}
