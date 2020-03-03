package com.solaris.guava;

import com.google.common.base.*;
import com.google.common.collect.Maps;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//guava字符串操作
public class MyString {
    public static void main(String[] args) {
        testJoin();
        testSplit();
        testCharMatcher();
        testCharSet();
        testCaseFormat();
        testStrings();
    }

    static void testStrings() {
        System.out.println();
        System.out.println("---begin testStrings");
        String str1 = "succ_1111333xx_succ";
        String str2 = "succ_3jfdkfds_succ";
        System.out.println("相同前缀:" + Strings.commonPrefix(str1, str2));
        System.out.println("相同后缀:" + Strings.commonSuffix(str1, str2));

        String emptyStr = "";
        System.out.println("emptyToNull:" + Strings.emptyToNull(emptyStr));
        System.out.println("isNullOrEmpty:" + Strings.isNullOrEmpty(emptyStr));

        System.out.println(Strings
                .lenientFormat("lenientFormat:obj1:%s,obje2:%s,obj3:%s", str1, str2, emptyStr));

        //pad
        String strPad= Strings.padEnd(emptyStr,10,'e');
        strPad=Strings.padStart(strPad,20,'b');
        System.out.println("padStart/End:"+strPad);
        System.out.println("repeat:"+Strings.repeat("12345 ", 10));
    }

    static void testCaseFormat() {
        // returns "constantName"
        CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "CONSTANT_NAME");
    }

    static void testCharSet() {
        String str = "魑魅魍魉";
        byte[] bytes = str.getBytes(Charsets.UTF_8);//deprecated
        byte[] bytes1 = str.getBytes(StandardCharsets.UTF_8);//正确
    }

    static void testCharMatcher() {
        System.out.println();
        System.out.println("---begin testCharMatcher");
        CharMatcher cm = CharMatcher.inRange('0', '9')
                .or(CharMatcher.javaIsoControl())
                .or(CharMatcher.breakingWhitespace())
                .or(CharMatcher.whitespace())
                .or(CharMatcher.ascii());
        CharMatcher cm1 = CharMatcher.forPredicate(c -> c > '4');
        System.out.println(CharMatcher.digit().retainFrom("123df45xx&"));
        //trim掉首位，其他匹配的折叠成一个
        System.out.println(CharMatcher.whitespace().
                trimAndCollapseFrom("  aa  b 11  ", '*'));
        System.out.println(CharMatcher.inRange('0', '9').
                trimAndCollapseFrom("121abc 11d ef345g789", '?'));
        System.out.println("1:" + cm1.matchesAllOf("1234"));
        System.out.println("2:" + cm1.matchesAllOf("789"));
        System.out.println("2:" + cm1.matchesAnyOf("156"));
    }

    static void testSplit() {
        System.out.println();
        System.out.println("---begin testSplit");
        String s = "|a||b|";

        //JDK
        String[] xx = s.split(",");
        System.out.println("jdk: " + Arrays.toString(s.split("|")));

        //GUAVA
        System.out.println("guava:↓");
        System.out.println("split:" + Splitter.on("|").trimResults().omitEmptyStrings().split(s));
        System.out.println("splitToList:" + Splitter.on("|").trimResults(CharMatcher.whitespace())
                .omitEmptyStrings().splitToList(s));
        System.out.println("map:" + Splitter.on("|").trimResults().omitEmptyStrings()
                .withKeyValueSeparator("=").split("1=2|3=4|5=8||,=?|||||"));//返回map
    }

    static void testJoin() {//主要处理对象为Null的情况
        System.out.println();
        System.out.println("---begin testJoin");

        List<String> list = Arrays.asList("1", "2", null, "3");
        //JDK== 1:2:null:3
        System.out.println(String.join(":", list));

        //GUAVA==1:2:3
        //Joiner
        Joiner joiner = Joiner.on(":").skipNulls();
        System.out.println(joiner.join(list));

        //MapJoiner
        Map<String, String> map = Maps.newHashMap();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", null);
        map.put(null, "v4");
        joiner.join(map.entrySet());
        System.out.println(joiner.join(list, map.values()));
        System.out.println(Joiner.on(",").withKeyValueSeparator("-").useForNull("nil").join(map));

        //appendTo
        StringBuilder builder = new StringBuilder();
        Joiner.on(",").skipNulls().appendTo(builder, list);
        builder.append(",");
        Joiner.on(",").withKeyValueSeparator("=").useForNull("nil").appendTo(builder, map);
        System.out.println("appendTo:" + builder.toString());
    }
}
