package com.solaris.javatest;

/*
    volatile的测试
 */
public class MyVolatile implements Runnable {
    boolean running = true;//volatile 能解决死循环
    int i = 0;

    @Override
    public void run() {
        while (running) {
            i++;
        }
        System.out.println("over1");
    }

    public static void main(String[] args) throws Exception {
        MyVolatile task = new MyVolatile();
        Thread th = new Thread(task);
        th.start();
        Thread.sleep(10);
        task.running = false;
        Thread.sleep(100);
        System.out.println(task.i);
        System.out.println("over");
    }
}
