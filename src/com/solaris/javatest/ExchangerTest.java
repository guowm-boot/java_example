package com.solaris.javatest;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 线程间交换数据
 * @author Administrator
 */
public class ExchangerTest {
    private static final Exchanger<String> exgr = new Exchanger<String>();
    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private static String str = "";

    public static void main(String[] args) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                str = "first";
                try {
                    String string = exgr.exchange(str);
                    System.out.println("first thread is :" + string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


        executorService.execute(new Runnable() {
            @Override
            public void run() {
                str = "second";
                try {
                    String string = exgr.exchange(str);
                    System.out.println("second thread :" + string);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
    }
}
