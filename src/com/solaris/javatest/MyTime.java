package com.solaris.javatest;

import java.time.LocalDate;
import java.time.LocalDateTime;

/*
    时间的解决方案
 */
public class MyTime {
    public static void main(String[] args) {
        LocalDate date=LocalDate.now();
        System.out.println("day of year:"+date.getDayOfYear()+
                " month:"+date.getDayOfMonth()+" week:"+date.getDayOfWeek());

    }
}
