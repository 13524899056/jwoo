package com.jwaoo.account.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class DateTimeUtils {

    /**
     * 获得当前时间的毫秒数.
     * @return
     */
    public static long currentTime() {
        return now().getLong(ChronoField.MILLI_OF_DAY);
    }

    public static long afterMinutes(int i) {
        return now().plusMinutes(i).getLong(ChronoField.MILLI_OF_DAY);
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static boolean isBeforNow(Long long1) {
        return System.currentTimeMillis() > long1;
    }

}
