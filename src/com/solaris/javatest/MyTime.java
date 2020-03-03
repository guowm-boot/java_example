package com.solaris.javatest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;

/*
    时间的解决方案
 */
public class MyTime {
    public static void main(String[] args) {
        localDate();
        localTime();
        localDateTime();
        testInstant();
        testDuration();
        testPeriod();
        testDateTimeFormatter();//string与对象转换
        testTimeZone();//时区
    }

    public static void testTimeZone() {
        System.out.print("\n---testDuration begin---\n");
        System.out.println(ZoneId.getAvailableZoneIds());//所有时区ID

        //东九区
        ZoneId d9 = ZoneId.of("+09:00");
        d9 = ZoneId.of("Asia/Shanghai");
        String d9ID = d9.getId();
        ZoneId d8 = ZoneId.of("+08:00");

        //LocalDateTime是不包含任何时区的时间
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime d9Time = LocalDateTime.ofInstant(localDateTime.atZone(d8).toInstant(), d9);
    }

    public static void testDateTimeFormatter() {
        System.out.print("\n---testDateTimeFormatter begin---\n");

        LocalDateTime ldt = LocalDateTime.now();
        //1、标准格式
        System.out.println("BASIC_ISO_DATE:" + DateTimeFormatter.BASIC_ISO_DATE.format(ldt));//20191211
        System.out.println("ISO_DATE_TIME:" + DateTimeFormatter.ISO_DATE_TIME.format(ldt));//2019-12-11T17:18:30.581
        System.out.println("ISO_LOCAL_DATE_TIME:" + DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ldt));//2019-12-11T17:18:30.581

        //2、本地格式
        DateTimeFormatter formatterLocal = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.CHINA);//可选
        System.out.println("MEDIUM:" + formatterLocal.format(ldt));
        // SHORT:  19-12-11 下午4:32
        // MEDIUM: 2019-12-11 16:33:13 ★★
        // LONG:  2019年12月11日 下午04时31分23秒

        //3、自定义格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String actualTimeStart = "2017/04/07 08:00:30";
        LocalDateTime timeStart = LocalDateTime.parse(actualTimeStart, formatter);
        System.out.println(timeStart.format(formatter));

        //4、枚举星期几
        for (DayOfWeek w : DayOfWeek.values())
            System.out.print(w.getDisplayName(TextStyle.SHORT, Locale.CHINA)
                    + " ");//星期一 星期二 星期三 星期四 星期五 星期六 星期日
    }

    //Duratio-描述时长,记录了相对秒数
    public static void testDuration() {
        System.out.println("---testDuration begin---");
        Duration d = Duration.between(LocalTime.of(19, 20), LocalTime.of(19, 30));
        d = d.abs();
        System.out.println(d.getSeconds());
        System.out.println(d.toDays());
        System.out.println(d.toHours());
        System.out.println("---testDuration   end---");
    }

    //类似于Duration，当是用于描述日级别以上的时长,存放了时长的年月日
    public static void testPeriod() {
        System.out.println("---testPeriod begin---");
        Period p = Period.between(LocalDate.of(2019, 10, 30),
                LocalDate.of(2019, 3, 4));
        System.out.println("year:" + p.getYears() + " month:" + p.getMonths() + " days:" + p.getDays());
        p = p.normalized();
        System.out.println("year:" + p.getYears() + " month:" + p.getMonths() + " days:" + p.getDays());
        System.out.println("---testPeriod   end---");
    }


    //描述UTC到当前的秒 Instant-瞬时
    public static void testInstant() {
        Instant i = Instant.now();
        i = Instant.ofEpochMilli(i.toEpochMilli());
    }

    public static void localDateTime() {
        System.out.println("---localDateTime begin---");
        LocalDateTime dateTimeOLd = LocalDateTime.of(2019, 10, 30, 11, 30, 50);
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDate d = LocalDate.now();
        LocalTime t = LocalTime.now();
        System.out.println("util:" + dateTimeOLd.until(LocalDateTime.now(), ChronoUnit.MONTHS));
        dateTime = LocalDateTime.of(d, t);
        dateTime = d.atTime(10, 30);
        dateTime = t.atDate(d);
        d = dateTime.toLocalDate();
        t = dateTime.toLocalTime();
        dateTime = dateTime.plusHours(20);
        dateTime = LocalDateTime.now().plusMinutes(1);
        dateTime = dateTime.with(TemporalAdjusters.next(DayOfWeek.of(7)));
        System.out.println(dateTime);
        System.out.println("---localDateTime end---");
    }

    public static TemporalAdjuster test1() {
        return (temporal) -> temporal.with(DAY_OF_MONTH, 1);
    }

    public static void localTime() {
        //表示11:30分
        LocalTime time = LocalTime.of(11, 30);
        //11:30:50
        LocalTime time2 = LocalTime.of(11, 30, 50);
        time2 = LocalTime.parse("20:14:30");
        System.out.println("时:" + time2.getHour());
        System.out.println("分:" + time2.getMinute());
        System.out.println("秒:" + time2.getSecond());

    }

    public static void localDate() {
        //获取当前日期
        LocalDate now = LocalDate.now();
        //获取指定年月日的localdate
        LocalDate date = LocalDate.of(2019, 10, 30);
        System.out.println("年：" + date.getYear());
        System.out.println("月:" + date.getMonthValue());
        System.out.println("几号:" + date.getDayOfMonth());
        System.out.println("星期几:" + date.getDayOfWeek().getValue());
        System.out.println("当年中的第几天:" + date.getDayOfYear());
        System.out.println("当月的天数:" + date.lengthOfMonth());
        System.out.println("当年的天数:" + date.lengthOfYear());
    }
}
