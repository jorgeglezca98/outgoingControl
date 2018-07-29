package com.example.jorgegonzalezcabrera.outgoing.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class utils {

    public static Date firstDateOfTheMonth(Date firstDayOfMonth) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(firstDayOfMonth);
        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1,0,0,0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}