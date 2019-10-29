package com.solaris.javatest;

/**
 * 内部类
 */

public class MyInnerClass {
    private int var1=0;
    private  String var2="";
    private  Inner aInner=new Inner();
    void  print1(){
        System.out.println("this outer class");
    }
    protected class Inner{
        private  String var2="ss";
        void print(){
            System.out.println("inner this inner class"+var1+MyInnerClass.this.var2);
        }

    }

    public static void main(String[] args) {
        MyInnerClass obj=new MyInnerClass();
        MyInnerClass.Inner i;
    }
}
