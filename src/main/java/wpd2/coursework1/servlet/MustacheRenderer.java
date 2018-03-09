package wpd2.coursework1.servlet;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

import java.io.IOException;
import java.io.StringWriter;

public class MustacheRenderer {
    private  static final String TEMPLATE_ROOT = "templates";

    private MustacheFactory factory;

    public MustacheRenderer() {
        this(TEMPLATE_ROOT);
    }

    public MustacheRenderer(String root) {
        factory = new DefaultMustacheFactory(root);
    }

    public String render(String templateName, Object model) {
        Mustache m = factory.compile(templateName);
        try (StringWriter writer = new StringWriter()) {
            m.execute(writer, model).close();
            return writer.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
