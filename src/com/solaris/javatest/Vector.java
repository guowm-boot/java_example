package com.solaris.javatest;

import java.util.ArrayList;

public class Vector {
    public static void main(String[] args) {
        ArrayList<Integer> myList=new ArrayList<>(1);
        myList.ensureCapacity(1);//扩大容量
        //myList.set(0,100);//error set的意义是替换
        myList.add(2);//尾部增加
        myList.add(100);
        int strIndex0= myList.get(0);
        System.out.println("size:"+myList.size()+" content:"+myList
        +" strIndex0:"+strIndex0);

        myList.trimToSize();//减少容量
    }
}
