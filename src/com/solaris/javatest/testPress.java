package com.solaris.javatest;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

//压力测试,笔记本慢
public class testPress {
    public static void main(String[] args) {
        ExecutorService es = Executors.newFixedThreadPool(16);
        AtomicLong num = new AtomicLong(0);
        for (int i = 0; i < 4; i++) {
            es.submit(() -> {
                int threadNum = 0;
                Instant beginTime=Instant.now();
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        threadNum++;
                    }
                } catch (Exception e) {
                }
                Instant endTime=Instant.now();
                num.addAndGet(threadNum);
                System.out.println("thread end:" + threadNum
                        +" mills:"+(endTime.toEpochMilli()-beginTime.toEpochMilli()));
            });
        }
        try {
            Thread.sleep(3000);
            es.shutdownNow();
            es.awaitTermination(3, TimeUnit.SECONDS);
        } catch (Exception e) { }
        System.out.println("end num:" + num+" nn:"+num.get()/10000/10000+"亿");
    }
}

