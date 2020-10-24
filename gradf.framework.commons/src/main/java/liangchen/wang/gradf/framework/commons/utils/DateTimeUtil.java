package liangchen.wang.gradf.framework.commons.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * @author LiangChen.Wang
 */
public enum DateTimeUtil {
    /**
     *
     */
    INSTANCE;
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYYMM = "yyyyMM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HH_MM_SS_SSS = "HH:mm:ss.SSS";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD);
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS);
    public static final DateTimeFormatter dateTimeMsFormatter = DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS_SSS);

    public String getYYYY_MM_DD_HH_MM_SS() {
        return getYYYY_MM_DD_HH_MM_SS(LocalDateTime.now());
    }

    public String getYYYY_MM_DD_HH_MM_SS(long ms) {
        Instant instant = Instant.ofEpochMilli(ms);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return getYYYY_MM_DD_HH_MM_SS(localDateTime);
    }

    public String getYYYY_MM_DD_HH_MM_SS(LocalDateTime date) {
        return dateTimeFormatter.format(date);
    }

    public String getYYYY_MM_DD_HH_MM_SS_SSS() {
        return getYYYY_MM_DD_HH_MM_SS_SSS(LocalDateTime.now());
    }

    public String getYYYY_MM_DD_HH_MM_SS_SSS(long ms) {
        Instant instant = Instant.ofEpochMilli(ms);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return getYYYY_MM_DD_HH_MM_SS(localDateTime);
    }

    public String getYYYY_MM_DD_HH_MM_SS_SSS(LocalDateTime date) {
        return dateTimeMsFormatter.format(date);
    }

    public String getYYYYMMDD() {
        LocalDate now = LocalDate.now();
        return now.format(dateFormatter);
    }

    public String getYYYY_MM_DD() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern(YYYY_MM_DD));
    }

    public String getYYYYMM() {
        LocalDate now = LocalDate.now();
        return now.format(DateTimeFormatter.ofPattern(YYYYMM));
    }

    public String getHH_MM_SS() {
        LocalTime now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern(HH_MM_SS));
    }

    public String getHH_MM_SS_SSS() {
        LocalTime now = LocalTime.now();
        return now.format(DateTimeFormatter.ofPattern(HH_MM_SS_SSS));
    }

    public String getCurrentDateToMinHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMin = LocalDateTime.of(now.toLocalDate(), LocalTime.MIN);
        return nowMin.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    public String getCurrentDateToMaxHour() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowMax = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
        return nowMax.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
    }

    public LocalDateTime utcDateTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public LocalDate utcDate() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    public LocalDateTime ofTimestamp(long timestamp, ZoneOffset zoneOffset) {
        return LocalDateTime.ofEpochSecond(timestamp, 0, zoneOffset);
    }
}
