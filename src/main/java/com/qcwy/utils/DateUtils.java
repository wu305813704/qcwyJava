package com.qcwy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by KouKi on 2017/2/18.
 */
public class DateUtils {
    private static SimpleDateFormat sdf;

    public static String format(Date date, String format) {
        sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date parse(String date, String format) throws ParseException {
        sdf = new SimpleDateFormat(format);
        return sdf.parse(date);
    }
}
