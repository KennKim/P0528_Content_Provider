package reversi.project.tki.p0528_content_provider.util;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import static java.lang.System.currentTimeMillis;


public class MyTime {

    public static final long LOCALE_GAP = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());
    public static int MINUTE, HOUR, DAY, WEEK, MONTH, YEAR;
    private static String NOW_STRING, SECOND_STRING, MINUTE_STRING, HOUR_STRING, DAY_STRING, WEEK_STRING, MONTH_STRING, YEAR_STRING;
    public static final int TIME_FORMAT = 1001;
    public static final int DATE_FORMAT = 1002;
    public static final int WEEK_FORMAT = 1003;


    public MyTime(Context context) {

        MINUTE = 60;
        HOUR = MINUTE * 60;
        DAY = HOUR * 24;
        WEEK = DAY * 7;
        MONTH = WEEK * 4;
        YEAR = MONTH * 12;

        NOW_STRING = "now";
        SECOND_STRING = "sec";
        MINUTE_STRING = "min";
        HOUR_STRING ="hours";
        DAY_STRING = "days";
        WEEK_STRING = "weeks";
        MONTH_STRING = "Months";
        YEAR_STRING = "Years";
    }

    public static String getString_GMT() {
        return currentTimeMillis() - LOCALE_GAP + "";
    }

    public static long getlong_GMT() {
        return currentTimeMillis() - LOCALE_GAP;
    }

    public static String getStringFormat(String dbTime, int format) {
        String str = "z";
        switch (format) {
            case TIME_FORMAT:
                str = "a hh:mm";
                break;
            case DATE_FORMAT:
                str = "MMM d, yyyy a hh:mm";
                break;
            case WEEK_FORMAT:
//                str = "EEE, MMM d, yyyy a HH:mm:ss";
//                str = "MMM d EEE,yyyy a HH:mm:ss";
                str = "MMM d EEE,yyyy a h:mm";
//        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy a HH:mm:ss");
                break;
        }
        DateFormat dateFormat = new SimpleDateFormat(str);

        long longTime = Long.parseLong(dbTime);
        return dateFormat.format(longTime);
    }

    public static String getStringElapsedTime(String dbTime) {
        long longTime = Long.parseLong(dbTime);
        long nowGMT = currentTimeMillis() - LOCALE_GAP;

        long elapsedTime = (nowGMT - longTime) / 1000;

        String result = null;

        if (elapsedTime >= MINUTE) {
            if (elapsedTime >= HOUR) {
                if (elapsedTime >= DAY) {
                    if (elapsedTime >= WEEK) {
                        if (elapsedTime >= MONTH) {
                            if (elapsedTime >= YEAR) {
//                                result = getStringFormat(dbTime);
                                result = (int) elapsedTime / YEAR + YEAR_STRING;
                            } else {
                                result = (int) elapsedTime / MONTH + MONTH_STRING;
                            }
                        } else {
                            result = (int) elapsedTime / WEEK + WEEK_STRING;
                        }
                    } else {
                        result = (int) elapsedTime / DAY + DAY_STRING;
                    }
                } else {
                    result = (int) elapsedTime / HOUR + HOUR_STRING;
                }
            } else {
                result = (int) elapsedTime / MINUTE + MINUTE_STRING;
            }
        } else {
            result = NOW_STRING;
        }

        return result;
    }
}
