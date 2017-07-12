package util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtil {

    static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    static final DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public static LocalDate strToDate(String dateStr) {
        return dateTimeFormatter.parseLocalDate(dateStr);
    }

    public static String dateToStr(LocalDate date) {
        return date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"));
    }

    public static String timestampToStr(Timestamp time) {
        return dateFormatter.format(time);
    }

    public static Timestamp strToTimestamp(String time) throws ParseException {
        dateFormatter.setTimeZone(TimeZone.getTimeZone("CST"));
        return new Timestamp(dateFormatter.parse(time).getTime());
    }
}
