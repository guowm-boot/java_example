package com.solaris.javatest;

import org.apache.poi.ss.formula.functions.Count;
import org.junit.Test;

import javax.xml.stream.events.Characters;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HashTest {
    private int a=0;
    private int b=0;
    private int c=0;
    private int[]   d={1,2,4,5};

    public static void main(String[] args) {
        HashTest obj=new HashTest();
        String str1="1111";
        String str2="22222";
        System.out.println("hash:"+str1.hashCode()+" "+str2.hashCode());
        System.out.println("object hash:"+new HashTest().hashCode());
        System.out.println(obj.d);
        System.out.println(Arrays.toString(obj.d));

    }

    @Override
    public int hashCode() {
        return Objects.hash(a,b,c,d);
    }

    @Test
    public void loadA26() throws Exception{
        List<String> strList = Files.readAllLines(Paths.get("D:\\文档\\开发\\汽车业务\\企标信号\\a26.txt"));
        HashSet<String> keySet = (HashSet) strList.stream().distinct().filter(s -> !s.isEmpty()).collect(Collectors.toSet());
        List<Integer> collect = keySet.stream().map(key -> {
            int h = key.hashCode();
            h = h ^ (h >>> 16);
            h = ((1<<10) - 1) & h;
            return h;
        }).collect(Collectors.toList());
        Map<Long, Long> indexCountMap = collect.stream().collect(Collectors.groupingBy(Integer::longValue, Collectors.counting()));
        System.out.println("hash bucket num:"+indexCountMap.size());
        System.out.println("max bin num:"+indexCountMap.values().stream().max(Long::compareTo).get());
        System.out.println("hash bin detail:"+indexCountMap);
        System.out.println("size:"+collect.size()+" "+collect);
    }
}
