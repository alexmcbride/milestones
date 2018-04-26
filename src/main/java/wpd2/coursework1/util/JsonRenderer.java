package wpd2.coursework1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.servlet.BaseServlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Renderer for producing JSON output.
 */
public class JsonRenderer {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    /**
     * Renders the specified object to the response as JSON.
     *
     * @param response the response to write the JSON to.
     * @param object the object to serialise as JSON.
     * @throws IOException error thown
     */
    public void render(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        LOG.info("JSON: " + json);
        response.getWriter().write(json);
    }
}
