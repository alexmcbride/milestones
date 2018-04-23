package wpd2.coursework1.helper;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/*
 * Helper for making times look all purty.
 */
public class PrettyTimeHelper {
    private PrettyTime prettyTime;

    /*
     * Creates a new PrettyTimeHelper.
     */
    public PrettyTimeHelper() {
        prettyTime = new PrettyTime();
    }

    /**
     * Formats a date as a pretty time string.
     *
     * @param date the date to format
     * @return the pretty string
     */
    public String format(Date date) {
        if (date == null) {
            return "Never";
        }
        return prettyTime.format(date);
    }
}
