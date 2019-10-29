package com.solaris.javatest;

class MySuper {
    protected String varProtect = "";

    void print() {
        System.out.println("this super class");
        String a;
    }

}

class MySub1 extends MySuper {
    @Override
    void print() {
        System.out.println("this sub class 1");
    }

    void testProtect(MySuper aMySuper) {
        String a = aMySuper.varProtect;
    }
}

public class MyExtend {
    public static void main(String[] args) {
        System.out.println();
    }

}
