package com.solaris.javatest;


//一个新的对象，应该重载的方法

import kafka.tools.DefaultMessageFormatter;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

class BaseObject {

}

public class MyObject extends BaseObject {
    private int x = 0;
    private String str;
    private String[] strArray;
    private List<String> strList;

    @Test
    public void test() {

        DefaultMessageFormatter x;
        MyObject other=new MyObject();
        System.out.println("equals result1:"+equals(other));

        MyObject other1=new MyObject();
        other1.strArray=new String[10];
        System.out.println("equals result1:"+ other1.equals(other));

//        Assert.assertEquals(testHash("abc1234567")
//                ,testHash("a1234567"));

        System.out.println(testHash("abcdefghijklmnjkdsjfskhijklmnjkdsjfska1234567"));
        System.out.println(testHash("4123456hijklmnjkdsjfskhijklmnjkdsjfska1234567"));
    }

    public int testHash(String str) {
        char[] value=str.toCharArray();
        int h=0;
        if (value.length > 0) {
            char val[] = value;
            for (int i = 0; i < value.length; i++) {
                h = 44 * h + val[i];
            }
        }
        return h;
    }

    public boolean equals(Object otherObject) {
        if (this == otherObject)
            return true;

        if (otherObject == null) return false;

        //or 子类可以重新定义相等的语义
        if (getClass() != otherObject.getClass())
            return false;
        //or 相等的定义在此类就定义，子类不会改变语义
        if (!(otherObject instanceof MyObject))
            return false;

        //如果需要比较基类
//        if (!super.equals(otherObject))
//            return false;

        MyObject other = (MyObject) otherObject;

        return x == other.x
                && Objects.equals(str, other.str)
                && Arrays.equals(strArray,other.strArray)
                && Objects.equals(strList,other.strList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, str, strArray, strList);
    }

    @Override
    public String toString() {
        return super.toString();
        //使用guava的方法 com.solaris.guava.Human.toString
    }
}
