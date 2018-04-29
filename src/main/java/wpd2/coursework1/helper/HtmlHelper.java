package wpd2.coursework1.helper;

import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.http.HttpServletResponse;

/*
 * Helper encapsulating stuff needed in HTML pages.
 */
public class HtmlHelper {
    private final HttpServletResponse response;

    /**
     * Creates a new HtmlHelper.
     *
     * @param response the response the HTML will be written to.
     */
    public HtmlHelper(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Escapes user input so it can be safely displayed.
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
