package csm;

import java.time.LocalDate;

import static org.junit.Assert.*;

/**
 * @author Thomas Povinelli
 *         Created 5/24/17
 *         In CourseSiteManager
 */
public class DateHybridTest {
    @org.junit.Test
    public void parse() throws Exception {
        String s = "2019-01-01";
        String t = "2017_05_24";
        String p = "2016-02-29";

        DateHybrid h1 = DateHybrid.parse(s);
        DateHybrid h2 = DateHybrid.parse(t);
        DateHybrid h3 = DateHybrid.parse(p);

        assertEquals(1, h1.getDate());
        assertEquals(24, h2.getDate());
        assertEquals(2017, h2.getYear());
        assertEquals(2019, h1.getYear());
        assertEquals(5, h2.getMonth());
        assertEquals(1, h1.getMonth());

        assertEquals(2016, h3.getYear());
        assertEquals(2, h3.getMonth());
        assertEquals(29, h3.getDate());

    }

    @org.junit.Test
    public void isWeekend() throws Exception {
        DateHybrid h1 = DateHybrid.parse("2017-05-20");
        DateHybrid h2 = DateHybrid.parse("2017-05-21");
        DateHybrid h3 = DateHybrid.parse("2017-05-22");
        DateHybrid h4 = DateHybrid.parse("2017-05-23");
        DateHybrid h5 = DateHybrid.parse("2016-05-20");

        assertTrue(h1.isWeekend());
        assertTrue(h2.isWeekend());
        assertFalse(h3.isWeekend());
        assertFalse(h4.isWeekend());
        assertFalse(h5.isWeekend());
    }

    @org.junit.Test
    public void isFriday() throws Exception {
        DateHybrid h1 = DateHybrid.parse("2016-05-20");
        DateHybrid h2 = DateHybrid.parse("2017-05-19");

        assertTrue(h1.isFriday());
        assertTrue(h2.isFriday());
    }

    @org.junit.Test
    public void isMonday() throws Exception {
        DateHybrid h1 = DateHybrid.parse("2016-05-16");
        DateHybrid h2 = DateHybrid.parse("2017-05-15");

        assertTrue(h1.isMonday());
        assertTrue(h2.isMonday());
    }

    @org.junit.Test
    public void toEpochSeconds() throws Exception {
        DateHybrid h1 = new DateHybrid(24, 5, 2017);
        DateHybrid h2 = new DateHybrid(24, 5, 2017, 12, 00, 00);
        DateHybrid h3 = new DateHybrid(LocalDate.now());
        DateHybrid h4 = new DateHybrid(System.currentTimeMillis());

        assertEquals(System.currentTimeMillis(), h1.toEpochSeconds(), 86400000);
        assertEquals(System.currentTimeMillis(), h3.toEpochSeconds(), 86400000);
        assertEquals(System.currentTimeMillis(), h4.toEpochSeconds(), 8640000);
        assertEquals(h2.toEpochSeconds(), h1.toEpochSeconds(), 86400000);
    }

    @org.junit.Test
    public void asLocalDate() throws Exception {
    }

    @org.junit.Test
    public void asLocalDateTime() throws Exception {
    }

    @org.junit.Test(expected = csm.exceptions.InvalidDateException.class)
    public void invalidDate() throws Exception {
        DateHybrid h8 = new DateHybrid(29, 2, 2017);
        assertEquals(29, h8.getDate());
        assertEquals(2, h8.getMonth());
        assertEquals(2017, h8.getYear());

        DateHybrid h = new DateHybrid(1, 3, 2017);
        assertEquals(h8.toEpochSeconds(), h.toEpochSeconds());

    }

    @org.junit.Test(expected = java.time.DateTimeException.class)
    public void testLocaleDateConstructor() throws Exception {
        DateHybrid h1 = new DateHybrid(LocalDate.of(2017, 2, 29));
        DateHybrid h2 = new DateHybrid(LocalDate.of(2017, 3, 1));

        assertEquals(h1.toEpochSeconds(), h2.toEpochSeconds());

    }

    @org.junit.Test
    public void getMonth() throws Exception {
        DateHybrid h1 = new DateHybrid(24, 5, 2017);
        DateHybrid h2 = new DateHybrid(24, 5, 2017, 12, 00, 00);
        DateHybrid h3 = new DateHybrid(LocalDate.now());
        DateHybrid h4 = new DateHybrid(System.currentTimeMillis());
        DateHybrid h5 = DateHybrid.parse("2016-05-20");
        DateHybrid h6 = DateHybrid.parse("2017-05-19");
        DateHybrid h7 = new DateHybrid(29, 2, 2016);



        assertEquals(5, h1.getMonth());
        assertEquals(5, h2.getMonth());
        assertEquals(5, h3.getMonth());
        assertEquals(5, h4.getMonth());
        assertEquals(5, h5.getMonth());
        assertEquals(5, h6.getMonth());
        assertEquals(2, h7.getMonth());
    }

    @org.junit.Test
    public void getDate() throws Exception {
        DateHybrid h1 = new DateHybrid(24, 5, 2017);
        DateHybrid h2 = new DateHybrid(24, 5, 2017, 12, 00, 00);
        DateHybrid h3 = new DateHybrid(LocalDate.now());
        DateHybrid h4 = new DateHybrid(System.currentTimeMillis());
        DateHybrid h5 = DateHybrid.parse("2016-05-20");
        DateHybrid h6 = DateHybrid.parse("2017-05-19");
        DateHybrid h7 = new DateHybrid(29, 2, 2016);



        assertEquals(24, h1.getDate());
        assertEquals(24, h2.getDate());
        assertEquals(24, h3.getDate());
        assertEquals(24, h4.getDate());
        assertEquals(20, h5.getDate());
        assertEquals(19, h6.getDate());
        assertEquals(29, h7.getDate());
    }

    @org.junit.Test
    public void getYear() throws Exception {
        DateHybrid h1 = new DateHybrid(24, 5, 2017);
        DateHybrid h2 = new DateHybrid(24, 5, 2017, 12, 00, 00);
        DateHybrid h3 = new DateHybrid(LocalDate.now());
        DateHybrid h4 = new DateHybrid(System.currentTimeMillis());
        DateHybrid h5 = DateHybrid.parse("2016-05-20");
        DateHybrid h6 = DateHybrid.parse("2017-05-19");
        DateHybrid h7 = new DateHybrid(29, 2, 2016);



        assertEquals(2017, h1.getYear());
        assertEquals(2017, h2.getYear());
        assertEquals(2017, h3.getYear());
        assertEquals(2017, h4.getYear());
        assertEquals(2016, h5.getYear());
        assertEquals(2017, h6.getYear());
        assertEquals(2016, h7.getYear());
    }

    @org.junit.Test
    public void asDate() throws Exception {
    }





}
