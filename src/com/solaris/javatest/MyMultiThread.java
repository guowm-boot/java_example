package com.solaris.javatest;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/*
    停止线程；终止线程
 */
class MyRunnable implements Runnable {
    private int loopTimes=0;
    private AtomicInteger times=new AtomicInteger(0);
    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                loopTimes++;
                System.out.println("MyRunnable running..."+loopTimes);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("MyRunnable is interrupt,interrupt status:"+Thread.currentThread().isInterrupted());
        } finally {
            System.out.println("MyRunnable end,loop:"+loopTimes);
        }
    }
}

//执行daemon线程
class CleanBufferTask implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                System.out.println("performe clean buff");
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }
        System.out.println("CleanBufferTask end");
    }
}

//直接抛出异常的类
class MyThrowException implements Runnable, Thread.UncaughtExceptionHandler {
    int status=0;
    @Override
    public void run() {
        try {
            throw new OutOfMemoryError("oom");
        } catch (Exception e) {

        }
        System.out.println("MyThrowException end:"+status);
    }

    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("recv uncatched exception,status:"+status+" ex:"+e);
    }
}

public class MyMultiThread {
    public static void main(String[] args) {
        try {
            testExcption();//catch线程中未捕获的异常

            MyRunnable r = new MyRunnable();
            Thread t = new Thread(r);
            t.start();
            Thread daemonThread = new Thread(new CleanBufferTask());
            daemonThread.setDaemon(true);
            daemonThread.start();
            Thread.sleep(3000);
            t.interrupt();
            for (int i = 0; i < 10; i++) {
                System.out.println("thread state:" + t.getState());
            }
            t.join();
            System.out.println("main method end");
        } catch (Exception e) {
        }
    }

    public static void testExcption() {
        MyThrowException r = new MyThrowException();
        Thread t = new Thread(r);
        Thread.setDefaultUncaughtExceptionHandler(r);
        //t.setUncaughtExceptionHandler(r);
        r.status=1;
        t.start();
    }
}
