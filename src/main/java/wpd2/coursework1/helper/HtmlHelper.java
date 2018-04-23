package wpd2.coursework1.helper;

import org.apache.commons.lang.StringEscapeUtils;

/*
 * Helper encapsulating stuff needed in HTML pages.
 */
public class HtmlHelper {
    /**
     * Encodes user input so it can be displayed in the page output.
     *
     * @param s the string to encode
     * @return the encoded output
     */
    public String encode(String s) {
        return StringEscapeUtils.escapeHtml(s);
    }
}
