package com.solaris.javatest;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyLockTask implements Runnable {
    private Lock myLock = new ReentrantLock();//可重入的锁
    private Condition myCond = myLock.newCondition();
    private List<String> strList = new LinkedList<>();
    private Object lockObj=new Object();

    //线程变量
    private  ThreadLocal<String> strThread=ThreadLocal.withInitial(()->new String(""));

    @Override
    public void run() {
        try {
            testLock();
        } catch (Exception e) {
        }
    }

    void testLock() throws InterruptedException {
        myLock.lock();
        try {
            while (strList.isEmpty())
                myCond.await();
            myLock.lockInterruptibly();
        } finally {
            myLock.unlock();
        }
    }

    synchronized void testSynLock() throws InterruptedException {
        while (strList.isEmpty())
            wait();
    }

    void testLock3 (){
        synchronized (lockObj){
            //do something
        }
    }
}

public class MyLock {
    public static void main(String[] args) {
    }
}
