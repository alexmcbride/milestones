package wpd2.coursework1.helper;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * Helper for making times look all purty.
 */
public class DateHelper {
    private static final SimpleDateFormat DUE_DATE_FORMAT = new SimpleDateFormat("d/M/YYYY @ HH:mm");

    private PrettyTime prettyTime;

    /*
     * Creates a new DateHelper.
     */
    public DateHelper() {
        prettyTime = new PrettyTime();
    }

    /**
     * Formats a date as a pretty time string.
     *
     * @param date the date to DUE_DATE_FORMAT
     * @return the pretty string
     */
    public String format(Date date) {
        if (date == null) {
            return "Never";
        }
        return prettyTime.format(date);
    }

    public String due(Date date) {
        if (date == null) {
            return "";
        }

        return DUE_DATE_FORMAT.format(date);
    }
}
