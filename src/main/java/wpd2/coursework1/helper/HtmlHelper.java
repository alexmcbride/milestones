package wpd2.coursework1.helper;

import org.apache.commons.lang.StringEscapeUtils;

/*
 * Helper encapsulating stuff needed in HTML pages.
 */
public class HtmlHelper {
    public String encode(String s) {
        return StringEscapeUtils.escapeHtml(s);
    }
}
