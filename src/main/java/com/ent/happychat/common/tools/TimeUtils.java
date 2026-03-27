package com.ent.happychat.common.tools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;

/**
 * LocalDateTime 工具类
 * 提供常用时间范围的起止点方法，覆盖：今天、昨天、本周、上周、本月、上个月
 */
public class TimeUtils {

    /** 一天中最晚的时间：23:59:59.999999999 */
    private static final LocalTime END_OF_DAY = LocalTime.of(23, 59, 59, 999_999_999);

    // ======================== 今天 ========================

    /**
     * 今天开始时间：今天 00:00:00.000000000
     */
    public static LocalDateTime startOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    /**
     * 今天结束时间：今天 23:59:59.999999999
     */
    public static LocalDateTime endOfToday() {
        return LocalDate.now().atTime(END_OF_DAY);
    }

    // ======================== 昨天 ========================

    /**
     * 昨天开始时间：昨天 00:00:00.000000000
     */
    public static LocalDateTime startOfYesterday() {
        return LocalDate.now().minusDays(1).atStartOfDay();
    }

    /**
     * 昨天结束时间：昨天 23:59:59.999999999
     */
    public static LocalDateTime endOfYesterday() {
        return LocalDate.now().minusDays(1).atTime(END_OF_DAY);
    }

    // ======================== 本周 ========================

    /**
     * 本周一开始时间：本周一 00:00:00.000000000
     */
    public static LocalDateTime startOfThisWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
    }

    /**
     * 本周日结束时间：本周日 23:59:59.999999999
     */
    public static LocalDateTime endOfThisWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .atTime(END_OF_DAY);
    }

    // ======================== 上周 ========================

    /**
     * 上周一开始时间：上周一 00:00:00.000000000
     */
    public static LocalDateTime startOfLastWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusWeeks(1)
                .atStartOfDay();
    }

    /**
     * 上周日结束时间：上周日 23:59:59.999999999
     */
    public static LocalDateTime endOfLastWeek() {
        return LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .minusDays(1)
                .atTime(END_OF_DAY);
    }

    // ======================== 本月 ========================

    /**
     * 本月第一天开始时间：本月 1 号 00:00:00.000000000
     */
    public static LocalDateTime startOfThisMonth() {
        return LocalDate.now()
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay();
    }

    /**
     * 本月最后一天结束时间：本月最后一天 23:59:59.999999999
     */
    public static LocalDateTime endOfThisMonth() {
        return LocalDate.now()
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(END_OF_DAY);
    }

    // ======================== 上个月 ========================

    /**
     * 上个月第一天开始时间：上月 1 号 00:00:00.000000000
     */
    public static LocalDateTime startOfLastMonth() {
        return LocalDate.now()
                .minusMonths(1)
                .with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay();
    }

    /**
     * 上个月最后一天结束时间：上月最后一天 23:59:59.999999999
     */
    public static LocalDateTime endOfLastMonth() {
        return LocalDate.now()
                .minusMonths(1)
                .with(TemporalAdjusters.lastDayOfMonth())
                .atTime(END_OF_DAY);
    }

    // ======================== 测试入口 ========================

    public static void main(String[] args) {
        System.out.println("=== 今天 ===");
        System.out.println("开始：" + startOfToday());
        System.out.println("结束：" + endOfToday());

        System.out.println("=== 昨天 ===");
        System.out.println("开始：" + startOfYesterday());
        System.out.println("结束：" + endOfYesterday());

        System.out.println("=== 本周 ===");
        System.out.println("开始：" + startOfThisWeek());
        System.out.println("结束：" + endOfThisWeek());

        System.out.println("=== 上周 ===");
        System.out.println("开始：" + startOfLastWeek());
        System.out.println("结束：" + endOfLastWeek());

        System.out.println("=== 本月 ===");
        System.out.println("开始：" + startOfThisMonth());
        System.out.println("结束：" + endOfThisMonth());

        System.out.println("=== 上个月 ===");
        System.out.println("开始：" + startOfLastMonth());
        System.out.println("结束：" + endOfLastMonth());
    }
}
