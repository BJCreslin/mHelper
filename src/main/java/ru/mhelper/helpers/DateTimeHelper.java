package ru.mhelper.helpers;

import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public final class DateTimeHelper {

    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDateAsString() {
        return java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
    }

    public static Date getCurrentDate() {
        return new Date();
    }

}
