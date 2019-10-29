package com.solaris.javatest;

import java.util.concurrent.*;

//并发控制
public class MyCurrent {
    public static void main(String[] args) throws  InterruptedException {
        Semaphore se=new Semaphore(1);
        CountDownLatch cdl=new CountDownLatch(3);
        CyclicBarrier cb=new CyclicBarrier(2);
        for (int i = 0; i < 12; i++) {
            se.release();
        }
        ExecutorService es= Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            int count=i;
            es.submit(()->{
                try {
                    se.acquire();
                    cdl.countDown();
                    cb.await();
                    System.out.println("thread:"+Thread.currentThread().getId()+count);
                }catch (Exception e){}
            });
        }
        cdl.await();
        es.shutdown();
    }
}
