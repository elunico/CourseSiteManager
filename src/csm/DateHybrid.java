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

/**
 * DateHybrid is a class meant to make it easy to transfer between {@link java.time.LocalDate}
 * {@link java.time.LocalDateTime}, {@link java.util.Date} objects and month, dayOfMonth,
 * year, hour, minute, and second integers.
 *
 * These objects can be constructed from any one of these date forms.
 * Note that time zones are handled by {@link ZoneId#systemDefault()} in all cases except
 * in the case of the constructor {@link DateHybrid(int, int, int)}
 * and @{link DateHybrid(int, int, int, int, int, int)} where {@link TimeZone#getDefault()}
 * is used by necessity since {@link LocalDate} can accept {@link ZoneId} but
 * {@link GregorianCalendar} cannot
 *
 * Note also that month is 1-12, where January = 1, February = 2, ... December = 12.
 * Please note that {@link java.time.Month#APRIL} for
 * instance is 3 but in {@link DateHybrid} April is represented as 4. Note also that
 * for January 1, {@link LocalDate} would say month == 0 but in {@link DateHybrid}
 * month == 1
 *
 * year is the normal year (i.e 2013) and
 * {@link #getDayOfMonth()} returns a number 1-31 based on the dayOfMonth of the month while
 * {@link #toDate()} returns a {@link java.util.Date} object
 *
 * When the object is constructed with time, then time is considered in equality
 * When the object is not constructed with time (that is LocalDateTime, epochMillis, or min, hour, second)
 * then time is not considered in the equality
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
    private final LocalDate localDate;
    private final LocalDateTime localDateTime;
    private int dayOfMonth, month, year, hour, minute, second;
    private final long epochMillis;
    private boolean timeSpecific;

    public DateHybrid(LocalDate date) {
        localDate = date;
        localDateTime = date.atTime(0, 0, 0);
        timeSpecific = false;
        long millis =
          localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        epochMillis = millis;
        initNumberConstantsFromMillis(millis);
        initDate(millis);
    }

    public DateHybrid(LocalDateTime dateTime) {
        localDate = dateTime.toLocalDate();
        localDateTime = dateTime;
        timeSpecific = true;
        long millis =
          localDateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        epochMillis = millis;
        initNumberConstantsFromMillis(millis);
        initDate(millis);
    }

    private void initNumberConstantsFromMillis(long millis) {
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone(ZoneId.systemDefault()));
        cal.clear();
        cal.setTimeInMillis(millis);
        this.month = cal.get(GregorianCalendar.MONTH) + 1;
        this.dayOfMonth = cal.get(GregorianCalendar.DAY_OF_MONTH);
        this.year = cal.get(GregorianCalendar.YEAR);
        this.second = cal.get(GregorianCalendar.SECOND);
        this.minute = cal.get(GregorianCalendar.MINUTE);
        this.hour = cal.get(GregorianCalendar.HOUR);
    }

    private void initDate(long millis) {
        date = new Date(millis);
    }

    public DateHybrid(long epochMillis) {
        timeSpecific = true;
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
        initDate(epochMillis);

    }

    public DateHybrid(int dayOfMonth, int month, int year)
      throws InvalidDateException
    {
        this(dayOfMonth, month, year, 0, 0, 0);
        timeSpecific = false;
    }

    public DateHybrid(int dayOfMonth, int month, int year, int hour, int minute, int second)
      throws InvalidDateException
    {
        if (invalidDayOfMonth(dayOfMonth, month, year) || month > 12 | month < 1) {
            throw new InvalidDateException(String.format(INVALID_DATE_EXCEPTION,
              dayOfMonth, month, year, hour, minute, second));
        }

        timeSpecific = true;

        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.clear();
        cal.set(year, month - 1, dayOfMonth + 1, hour, minute, second);
        this.epochMillis = cal.getTimeInMillis();
        this.localDate =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDate();
        this.localDateTime =
          Instant.ofEpochMilli(epochMillis)
                 .atZone(ZoneId.systemDefault())
                 .toLocalDateTime();
        initDate(epochMillis);
    }

    private boolean invalidDayOfMonth(int date, int month, int year) {
        if (date < 1 || date > 31) {
            return true;
        }
        if (date == 31) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    return false;
                case 2:
                case 4:
                case 6:
                case 9:
                case 11:
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

    public DateHybrid(DateHybrid forCopy) {
        this(forCopy.epochMillis);
        timeSpecific = forCopy.timeSpecific;
    }

    public boolean isTimeSpecific() {
        return timeSpecific;
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // ignore
        }
        return null;
    }

    String getMonthName() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return months[getMonth() - 1];
    }

    public String toString() {
       return String.format("DateHybrid(%s %d, %d: %02d:%02d:%02d)", getMonthName(), getDayOfMonth(), getYear(), getHour(), getMinute(), getSecond());
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

    public long toEpochMillis() {
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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public int getYear() {
        return year;
    }

    public Date toDate() {
        return new Date(epochMillis);
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != getClass()) return false;

        DateHybrid that = (DateHybrid)obj;

        return (timeSpecific && that.epochMillis == this.epochMillis) ||
               (!timeSpecific && (localDate.equals(that.localDate)));
    }

    public static DateHybrid now() {
        return new DateHybrid(System.currentTimeMillis());
    }
}
