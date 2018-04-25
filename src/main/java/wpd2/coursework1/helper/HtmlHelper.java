package wpd2.coursework1.helper;

import org.apache.commons.lang.StringEscapeUtils;
import org.jboss.netty.handler.codec.http.HttpResponse;

import javax.servlet.http.HttpServletResponse;

/*
 * Helper encapsulating stuff needed in HTML pages.
 */
public class HtmlHelper {
    private final HttpServletResponse response;

    public HtmlHelper(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Encodes user input so it can be displayed in the page output.
     *
     * @param s the string to encode
     * @return the encoded output
     */
    public String encode(String s) {
        return StringEscapeUtils.escapeHtml(s);
    }

    /**
     * Encodes a URL to add session stuff.
     *
     * @param s the URL to encode.
     * @return an encoded URL.
     */
    public String url(String s) {
        return response.encodeURL(s);
    }
}
