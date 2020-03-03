package com.solaris.guava;

import com.google.common.util.concurrent.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;

//guava的多线程工具
public class MyConcurrent {
    public static void main(String[] args) {
        guavaTest();//guava的使用方法
        testChain();//链式调用
        testException();//调用链的异常处理
        completedFutureExample();
        completeExceptionallyExample();//调用链异常的处理
        interruptExample();//测试中断future
    }


    static void interruptExample() {
        CompletableFuture<String> cf1 = CompletableFuture.completedFuture("Message").thenApplyAsync(s-> {
            boolean x = true;
            while (x) {
            }
            return s.toUpperCase();
        });
        System.out.println();
        System.out.println("---begin interrupt");
        CompletableFuture<String> cf2 = cf1.exceptionally(throwable -> "exception message:" + throwable);
        cf1.complete("interrupt");//返回值
        //cf1.completeExceptionally(new IllegalArgumentException("interrupt exception"));//返回异常
        System.out.println("xxxxx="+cf2.join());
    }

    static void completeExceptionallyExample() {
        CompletableFuture<String> cf = CompletableFuture.completedFuture("message")
                .thenApplyAsync(s1 -> {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                            }
                            return s1.toUpperCase();
                        }
                );
        CompletableFuture<String> exceptionHandler = cf.handle((s, th) -> {
            return (th != null) ? "message upon cancel" : "";
        });
        cf.completeExceptionally(new RuntimeException("completed exceptionally"));
        assertTrue("Was not completed exceptionally", cf.isCompletedExceptionally());
        try {
            cf.join();
            fail("Should have thrown an exception");
        } catch (CompletionException ex) { // just for testing
            assertEquals("completed exceptionally", ex.getCause().getMessage());
        }
        assertEquals("message upon cancel", exceptionHandler.join());
        System.out.println("end");
    }

    static void completedFutureExample() {
        CompletableFuture cf = CompletableFuture.completedFuture("message");
        assertTrue(cf.isDone());
        assertEquals("message", cf.getNow(null));
    }

    private static void testException() {
        System.out.println();
        System.out.println("---begin testException,thread:" + Thread.currentThread().getId());
        ExecutorService service = ForkJoinPool.commonPool();
        //出现异常，则不执行后续流程
        CompletableFuture.supplyAsync(() -> MyConcurrent.funcExcepion())
                .thenRun(() -> System.out.println("no run,thread:" + Thread.currentThread().getId()));

        //处理异常
        CompletableFuture.supplyAsync(() -> MyConcurrent.funcExcepion(), service).
                whenCompleteAsync((s, t) -> {
                    System.out.println(s + " " + t + " thread:" + Thread.currentThread().getId());
                }, service);

        CompletableFuture.supplyAsync(() -> MyConcurrent.sleepExcetpion(1000), service)
                .runAfterBothAsync(CompletableFuture.runAsync(() -> MyConcurrent.sleepExcetpion(6000))
                        , () -> System.out.println("xxxx1"), service);
        //service.shutdown();
    }

    private static String sleepExcetpion(long mil) {
        try {
            Thread.sleep(mil);
            if (mil < 1000 * 10)
                throw new Exception("err");
        } catch (Exception e) {
        }
        return "";
    }

    private static String funcExcepion() {
        System.out.println("begin fnA,thread:" + Thread.currentThread().getId());
        if (true)
            throw new IllegalArgumentException("err");
        return "hello";
    }

    private static void testChain() {
        Executor executor = ForkJoinPool.commonPool();
        //thenApply -关心上次返回值，这次有返回值
        String result = CompletableFuture.supplyAsync(() -> "hello", executor).thenApply(s -> s + " world").join();
        System.out.println(result);
        //thenAccept-关心上次返回值，这次无返回值
        CompletableFuture.supplyAsync(() -> "hello", executor).thenAccept(s -> System.out.println(s + " world1"));
        //then run-不关心之前的返回值,这次无返回值
        CompletableFuture.supplyAsync(() -> "hello ", executor).thenRun(() -> System.out.println(" then run"));
    }

    private static void guavaTest() {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        for (int i = 0; i < 30; i++) {
            final int index = i;
            ListenableFuture<String> future = service.submit(() -> {
                        return "result of task:" + index + " tid:" + Thread.currentThread().getId();
                    }
            );
            Futures.addCallback(future, new FutureCallback<String>() {
                        public void onSuccess(String explosion) {
                            System.out.println("begin callback,pre result:" + explosion + " tid:" + Thread.currentThread().getId());
                        }

                        public void onFailure(Throwable thrown) {
                        }
                    }
                    , service);
        }
        try {
            service.awaitTermination(1, TimeUnit.SECONDS);
        } catch (Exception e) {
        }
        service.shutdown();
    }
}
