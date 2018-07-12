package com.example.jorgegonzalezcabrera.outgoing.utilities;

import java.util.Date;
import java.util.GregorianCalendar;

public class utils {

    public static Date firstDateOfTheMonth(Date firstDayOfMonth) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(firstDayOfMonth);
        c.set(GregorianCalendar.DAY_OF_MONTH, 1);
        c.set(GregorianCalendar.HOUR_OF_DAY, 0);
        c.set(GregorianCalendar.MINUTE, 0);
        c.set(GregorianCalendar.SECOND, 0);
        c.set(GregorianCalendar.MILLISECOND, 0);
        firstDayOfMonth = c.getTime();
        return firstDayOfMonth;
    }
}
