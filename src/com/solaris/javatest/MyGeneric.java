package com.solaris.javatest;

/*
    我的泛型
 */


import org.apache.poi.ss.formula.functions.T;

class ArrayAlg {
    public static <T> T getMiddle(T... obj) {
        return obj[obj.length / 2];
    }

    public static <T extends Comparable & java.io.Serializable> T min(T[] a) {
        if (a == null || a.length == 0) return null;
        T smallest = a[0];
        for (int i = 1; i < a.length; i++)
            if (smallest.compareTo(a[i]) > 0) smallest = a[i];
        return smallest;
    }
    public static   void setValue(Plate<? extends MotherClass> a){
    }
}

class MotherClass{

}

class ChildClass{

}

class Plate<T> {
    private T item;
    public Plate(T t) {
        item = t;
    }
    public void set(T t) {
        item = t;
    }
    public T get() {
        return item;
    }
}

public class MyGeneric {
    public static void main(String[] args) {
        String[] strArray = {"solaris", "kin", "tim", "tanke"};
        System.out.println("middle is:" + ArrayAlg.<String>getMiddle(strArray));
        System.out.println("middle simple is:" + ArrayAlg.getMiddle(strArray));
        System.out.println("min is:" + ArrayAlg.min(strArray));
    }

}
