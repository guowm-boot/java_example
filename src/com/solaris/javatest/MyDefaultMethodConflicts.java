package com.solaris.javatest;

interface MyInterface {
    default void print() {
        System.out.println("this is interface A");
    }
}

interface MyInterface1 {
    default void print() {
        System.out.println("this is interface B");
    }
}

class MySuperClass {
    public void print() {
        System.out.println("this is super class");
    }
}

class ConflictTest1 extends MySuperClass
        implements MyInterface,MyInterface1 { };

class ConflictTest2
        implements MyInterface,MyInterface1 {

    @Override
    public void print() {
        MyInterface1.super.print();
    }
};

public class MyDefaultMethodConflicts  {

    public static void main(String[] args) {
        {
            ConflictTest1 obj=new ConflictTest1();
            obj.print();
        }
        {
            ConflictTest2 obj=new ConflictTest2();
            obj.print();
        }

    }
}
