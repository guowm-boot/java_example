package com.solaris.guava;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//测试volatile
public class MyVolatile {
    //如果不使用volatile,则每个while循环的if条件则不会满足
    private static volatile   C c = new C();

    static long longvar=0;

    public static void main(String[] args) {
        ExecutorService es = Executors.newCachedThreadPool();
        testVolatile(es);//测试volatile的可见性
        //testAtomic(es);//测试long类型的原子性
    }

    //longvar必须为0或-1中的一个，即不会输出errlog
    private static void testAtomic(ExecutorService es) {
        MyVolatile a=new MyVolatile();
        es.submit(() -> a.methodAtomicA(0));
        es.submit(() -> a.methodAtomicA(-1));
        while (longvar != 0 && longvar != -1)
        {
            System.out.println("err:"+longvar);
        }
    }
    private static void testVolatile(ExecutorService es) {
        es.submit(MyVolatile::method1);
        es.submit(MyVolatile::method2);
        es.submit(MyVolatile::method3);
    }

    static void method1(){
        while (true) {
            if (c.x == 'A') {
                System.out.println('A');
                c.x = 'B';
            }
        }
    }
    static void method2(){
        while (true) {
            if (c.x == 'B') {
                System.out.println('B');
                c.x = 'C';
            }
        }
    }
    static void method3(){
        while (true) {
            if (c.x == 'C') {
                System.out.println('C');
                c.x = 'A';
            }
        }
    }

    void methodAtomicA(long updateValue){
        while (!Thread.interrupted()) {
            longvar=updateValue;
        }
    }
}


class C {
    public char x = 'A';
}