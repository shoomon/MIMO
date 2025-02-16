package com.bisang.backend.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

public class DateUtils {
    /**
     * LocalDateTime을 Date로 변환 (클라이언트한테 넘길 때 사용)
     * @param localDateTime
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * Date를 LocalDateTime으로 변환 (클라이언트에서 받아서 저장할 때 사용)
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(String date) {
        DateTimeFormatter formatter =
                new DateTimeFormatterBuilder()
                        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                        .optionalStart()
                        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                        .optionalEnd()
                        .toFormatter();

        return LocalDateTime.parse(date, formatter);
    }
}
