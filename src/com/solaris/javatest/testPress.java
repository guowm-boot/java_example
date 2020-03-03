package com.solaris.javatest;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

//压力测试,笔记本慢
public class testPress {
    public static void main(String[] args) {
        int threads=4;
        long runTime=10*1000;
        long fakeNum=0;
        LongAdder costTime=new LongAdder();
        LongAdder adder=new LongAdder();
        if (args.length > 0){
            threads=Integer.parseInt(args[0]);
        }
        if (args.length > 1){
            runTime=Integer.parseInt(args[1])*1000;
        }
        ExecutorService es = Executors.newFixedThreadPool(threads);
        AtomicLong num = new AtomicLong(0);
        for (int i = 0; i < threads; i++) {
            es.submit(() -> {
                long threadNum = 0;
                Instant beginTime=Instant.now();
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        //threadNum++;  //最快
                        //adder.increment();//一般
                        num.incrementAndGet();//最慢

                    }
                } catch (Exception e) {
                }
                Instant endTime=Instant.now();
                costTime.add(endTime.toEpochMilli()-beginTime.toEpochMilli());
                num.addAndGet(threadNum);
                System.out.println("thread end:" + threadNum
                        +" mills:"+(endTime.toEpochMilli()-beginTime.toEpochMilli()));
            });
        }
        try {
            Thread.sleep(runTime);
            es.shutdownNow();
            es.awaitTermination(3, TimeUnit.SECONDS);
        } catch (Exception e) { }
        long realNum=num.get()+adder.sum();
        long realTime=costTime.sum();
        System.out.println("end num:" + realNum+" per second:"+realNum/10000/10000/(realTime/1000.0/threads)+"亿"+
                " avg per s/core:"+realNum/10000.0/10000/(realTime/1000.0)/threads);
    }
}

