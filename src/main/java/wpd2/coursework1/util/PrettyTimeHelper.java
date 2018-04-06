package wpd2.coursework1.util;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

public class PrettyTimeHelper {
    private PrettyTime prettyTime;

    public PrettyTimeHelper() {
        prettyTime = new PrettyTime();
    }

    public String format(Date date) {
        return prettyTime.format(date);
    }
}
