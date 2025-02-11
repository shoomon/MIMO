package com.bisang.backend.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    /**
     * LocalDateTime을 Date로 변환 (클라이언트한테 넘길 때 사용)
     * @param localDateTime
     * @return Date
     */
    public static Date LocalDateTimeToDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }

    /**
     * Date를 LocalDateTime으로 변환 (클라이언트에서 받아서 저장할 때 사용)
     * @param date
     * @return LocalDateTime
     */
    public static LocalDateTime DateToLocalDateTime(String date) {

        Instant instant = Instant.parse(date);
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        return localDateTime;
    }
}
