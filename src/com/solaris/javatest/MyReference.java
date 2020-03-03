package com.solaris.javatest;

import org.junit.Test;

import java.lang.ref.*;

//测试 软引用、弱引用、虚引用
public class MyReference {
    @Test
    public void testWeakReference() throws Exception {
        ReferenceQueue<String> rq = new ReferenceQueue<>();
        //这里必须用new String构建字符串，而不能直接传入字面常量字符串
        Reference<String> r = new WeakReference<>(new String("java"), rq);
        Reference rf;

        //一次System.gc()并不一定会回收A，所以要多试几次
        while((rf=rq.poll()) == null) {
            System.gc();
        }
        System.out.println(rf);
        if (rf != null) {
            //引用指向的对象已经被回收，存入引入队列的是弱引用本身,所以这里最终返回null
            System.out.println(rf.get());//null
        }
        System.out.println(r.getClass().getName());


    }

    @Test
    public void testSoftReference() {//不是即将oom引起的GC，所以不会回收
        ReferenceQueue<String> referenceQueue = new ReferenceQueue<>();
        SoftReference<String> softReference = new SoftReference<>("abc", referenceQueue);
        System.gc();
        System.out.println(softReference.get());//abc
        Reference<? extends String> reference = referenceQueue.poll();
        System.out.println(reference);//null
    }

    //虚引用
    @Test
    public void testPhantomReference() {
        ReferenceQueue<String> rq = new ReferenceQueue<>();
        PhantomReference<String> reference = new PhantomReference<>(new String("cord"), rq);
        System.out.println(reference.get());
        System.gc();
        System.runFinalization();
        System.out.println(rq.poll() == reference);//true
    }
}
