package com.solaris.javatest;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MySafeCollection {
    public static void main(String[] args) {
        ConcurrentHashMap<String, Integer> strLongMap = new ConcurrentHashMap<>();
        strLongMap.compute("1", (k, v) -> v == null ? 1 : 4);
        System.out.println(strLongMap);
        strLongMap.merge("1", 3, (o, n) -> n + 4);
        System.out.println(strLongMap);
        Set<String> strSet = ConcurrentHashMap.newKeySet();
        strSet = strLongMap.keySet(3);
        strSet.add("124");
        strSet.forEach((k) -> System.out.println(k));
        MyParallelArrayAlgorithms.sort();
    }
}

class MyParallelArrayAlgorithms {
    void test() {
        try {
            String contents = new String(Files.readAllBytes(
                    Paths.get("alice.txt")), StandardCharsets.UTF_8); // Read file into string
            String[] words = contents.split("[\\P{L}]+"); // Split along nonletters
            Arrays.parallelSort(words);
        } catch (Exception e) {
        }
    }

    static void sort(){
        ArrayList<String> strList = new ArrayList<>();
        strList.add("111");
        strList.add("2");
        strList.sort(Comparator.comparing(String::length));;
    }
}