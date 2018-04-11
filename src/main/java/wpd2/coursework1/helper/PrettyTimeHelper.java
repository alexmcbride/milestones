package wpd2.coursework1.helper;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/*
 * Helper for making times look all purty.
 */
public class PrettyTimeHelper {
    private PrettyTime prettyTime;

    public PrettyTimeHelper() {
        prettyTime = new PrettyTime();
    }

    public String format(Date date) {
        if (date == null) {
            return "Never";
        }
        return prettyTime.format(date);
    }
}
