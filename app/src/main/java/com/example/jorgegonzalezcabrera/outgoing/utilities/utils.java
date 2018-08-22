package com.example.jorgegonzalezcabrera.outgoing.utilities;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class utils {

    public static int dpToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics));
    }

    public static Date firstDateOfTheMonth(Date firstDayOfMonth) {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(firstDayOfMonth);
        c.set(c.get(Calendar.YEAR),c.get(Calendar.MONTH),1,0,0,0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
}