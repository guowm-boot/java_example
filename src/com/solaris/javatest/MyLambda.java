package com.solaris.javatest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;

public class MyLambda {

    public static void main(String[] args) {
        try {
            Comparator<String> comp =
                    (f, s) ->
                            f.length() - s.length();

            String[] aString = new String[]{"1", "2", "3", "11", "23"};
            Arrays.sort(aString);
            System.out.println(Arrays.toString(aString));

            Comparator<String> cc = (a, b) -> a.length() - b.length();
            Arrays.sort(aString, cc);
            Arrays.sort(aString, (a, b) -> a.length() - b.length());
            Arrays.sort(aString, String::compareTo);
            System.out.println(Arrays.toString(aString));

            BiFunction<String, String, Integer> a = (x, y) -> x.length() - y.length();

            //函数的引用
            MyLambda obj = new MyLambda();
            Timer aTimer = new Timer(1000, obj::print);
            aTimer.start();
            Thread.sleep(10000);
        } catch (Exception e) {
        }
    }

    void print(ActionEvent e) {
        System.out.println("performed");
        throw new RuntimeException("aaa");
    }
}
