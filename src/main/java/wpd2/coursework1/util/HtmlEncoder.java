package wpd2.coursework1.util;

import org.apache.commons.lang.StringEscapeUtils;

public class HtmlEncoder {
    public String encode(String s) {
        return StringEscapeUtils.escapeHtml(s);
    }
}
