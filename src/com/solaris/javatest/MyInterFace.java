package com.solaris.javatest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyInterFace implements Comparable<MyInterFace>, ActionListener {
    public int compareTo(MyInterFace o) {
        return 0;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("actionPerformed");
    }

    public static void main(String[] args) {
        try {
            javax.swing.Timer t = new javax.swing.Timer(1000, new MyInterFace());
            t.start();
            Thread.sleep(10 * 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}