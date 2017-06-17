package csm;

import csm.exceptions.InvalidDateException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static java.lang.Integer.parseInt;

/**
 * @author Thomas Povinelli
 *         Created 5/2/17
 *         In Homework6
 */
public class DateHybrid
  implements Comparable<DateHybrid>, Serializable, Cloneable
{

    public static final DateHybrid WEEKEND;
    private static final String
      INVALID_DATE_EXCEPTION = "Date components Date: %d, Month: %d, " +
                               "Year: %d, Hour: %d, Minute: %d, Second: %d " +
                               "are not valid";

    static {
        DateHybrid WEEKEND1;
        try {
            WEEKEND1 = new DateHybrid(4, 30, 2017);
        } catch (InvalidDateException e) {
            // squelch
            WEEKEND1 = null;
        }
        WEEKEND = WEEKEND1;
    }

    private Date date;
    private LocalDate localDate;
    private LocalDateTime localDateTime;
    private int day, month, year, hour, minute, second;
    private long epochMillis;

    public DateHybrid(LocalDate date) {
        localDate = date;
        localDateTime = date.atTime(0, 0, 0);
        long millis =
          localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        epochMillis = millis;
        initNumberConstantsFromMillis(millis);
        initDate();
    }

    private void initNumberConstantsFromMillis(long millis) {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("EDT"));
        cal.clear();
        cal.setTimeInMillis(millis);
        this.month = cal.get(GregorianCalendar.MONTH) + 1;
        this.day = cal.get(GregorianCalendar.DAY_OF_MONTH);
        this.year = cal.get(GregorianCalendar.YEAR);
        this.second = cal.get(GregorianCalendar.SECOND);
        this.minute = cal.get(GregorianCalendar.MINUTE);
        this.hour = cal.get(GregorianCalendar.HOUR);
    }

    private void initDate() {
        date = new Date(epochMillis);
    }

    public DateHybrid(long epochMillis) {
        this.epochMillis = epochMillis;
        this.localDate =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDate();
        this.localDateTime =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDateTime();
        initNumberConstantsFromMillis(epochMillis);
        initDate();

    }

    public DateHybrid(int date, int month, int year)
      throws InvalidDateException
    {
        this(date, month, year, 0, 0, 0);
    }

    public DateHybrid(int date, int month, int year, int hour, int minute, int second)
      throws InvalidDateException
    {
        if (invalidDate(date, month, year) || month > 12 | month < 1) {
            throw new InvalidDateException(String.format(INVALID_DATE_EXCEPTION,
              date, month, year, hour, minute, second));
        }

        this.day = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("EDT"));
        cal.clear();
        cal.set(year, month - 1, date + 1, hour, minute, second);
        this.epochMillis = cal.getTimeInMillis();
        this.localDate =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDate();
        this.localDateTime =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDateTime();
        initDate();
    }

    private boolean invalidDate(int date, int month, int year) {
        if (date < 1) {
            return true;
        }
        if (date > 31) {
            return true;
        }
        if (date == 31) {
            switch (month) {
                case 2:
                case 4:
                case 6:
                case 9:
                case 11:
                    return true;
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return false;
                default:
                    return true;
            }
        }
        if (month == 2) {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
                return date > 29;
            } else {
                return date > 28;
            }
        }
        return false;
    }

    public static DateHybrid parse(String yyyy_mm_dd)
      throws InvalidDateException
    {
        String[] comps = yyyy_mm_dd.split("[_-]");
        // LocalDate String is used to save and it has format YYYY-MM-dd as a string
        return new DateHybrid(parseInt(comps[2]), parseInt(comps[1]), parseInt(comps[0]));
    }

    public DateHybrid deepCopy() {
        return new DateHybrid(epochMillis);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // ignore
        }
        return null;
    }

    public String toString() {
        return localDate.toString();
    }

    public boolean isWeekend() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.clear();
        cal.setTimeInMillis(epochMillis);
        int val = cal.get(GregorianCalendar.DAY_OF_WEEK);
        return val == 1 || val == 7;
    }

    public boolean isFriday() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.clear();
        cal.setTimeInMillis(epochMillis);
        int val = cal.get(GregorianCalendar.DAY_OF_WEEK);
        return val == 6;
    }

    public boolean isMonday() {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.clear();
        cal.setTimeInMillis(epochMillis);
        int val = cal.get(GregorianCalendar.DAY_OF_WEEK);
        return val == 2;
    }

    public long toEpochSeconds() {
        return epochMillis;
    }

    public LocalDate asLocalDate() {
        return localDate;
    }

    public LocalDateTime asLocalDateTime() {
        return localDateTime;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return day;
    }

    public int getYear() {
        return year;
    }

    public Date asDate() {
        return date;
    }

    @Override
    public int compareTo(@NotNull DateHybrid o) {
        if (epochMillis < o.epochMillis) {
            return -1;
        } else if (epochMillis > o.epochMillis) {
            return 1;
        } else {
            return 0;
        }
    }

    public static DateHybrid now() {
        return new DateHybrid(System.currentTimeMillis());
    }
}
