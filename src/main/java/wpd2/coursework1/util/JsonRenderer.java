package wpd2.coursework1.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wpd2.coursework1.servlet.BaseServlet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonRenderer {
    @SuppressWarnings("unused")
    static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    public void render(HttpServletResponse response, Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);
        LOG.info(json);
        response.getWriter().write(json);
    }
}
