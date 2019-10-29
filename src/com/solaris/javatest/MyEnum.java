package com.solaris.javatest;


import java.util.Arrays;

enum  Es
{
    SMALL("1"),
    MEDIA("2"),
    LARGE("3");
    private String abb;

    private Es(String abbreviation) { this.abb= abbreviation; }
    public String getAbbreviation() { return abb; }
}

public class MyEnum {
    public static void main(String[] args) {
        System.out.println(Es.SMALL.toString());
        Es a= Es.valueOf("LARGE");
        System.out.println(a.toString());
        System.out.println(Arrays.toString(Es.values()));//列出所有枚举
    }
}
